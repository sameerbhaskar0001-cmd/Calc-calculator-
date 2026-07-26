package com.example

import android.content.Context
import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.http.*
import java.io.File
import java.io.InputStream
import java.net.URLDecoder
import java.util.concurrent.TimeUnit

interface GoogleOAuthApi {
    @FormUrlEncoded
    @POST("token")
    suspend fun exchangeCode(
        @Field("code") code: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("grant_type") grantType: String = "authorization_code"
    ): ResponseBody

    @FormUrlEncoded
    @POST("token")
    suspend fun refreshToken(
        @Field("refresh_token") refreshToken: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String = "refresh_token"
    ): ResponseBody
}

interface GoogleDriveApi {
    @GET("files")
    suspend fun listFiles(
        @Header("Authorization") authHeader: String,
        @Query("q") query: String,
        @Query("fields") fields: String = "files(id, name, createdTime, size)"
    ): ResponseBody

    @Multipart
    @POST("https://www.googleapis.com/upload/drive/v3/files?uploadType=multipart")
    suspend fun uploadFileMultipart(
        @Header("Authorization") authHeader: String,
        @Part metadata: MultipartBody.Part,
        @Part file: MultipartBody.Part
    ): ResponseBody

    @GET("files/{fileId}")
    suspend fun downloadFile(
        @Header("Authorization") authHeader: String,
        @Path("fileId") fileId: String,
        @Query("alt") alt: String = "media"
    ): ResponseBody

    @DELETE("files/{fileId}")
    suspend fun deleteFile(
        @Header("Authorization") authHeader: String,
        @Path("fileId") fileId: String
    ): ResponseBody

    @GET("https://www.googleapis.com/oauth2/v3/userinfo")
    suspend fun getUserInfo(
        @Header("Authorization") authHeader: String
    ): ResponseBody
}

class GoogleDriveManager(private val context: Context) {
    private val TAG = "GoogleDriveManager"
    private val prefs = context.getSharedPreferences("google_drive_prefs", Context.MODE_PRIVATE)

    // Default Client Credentials for general convenience.
    // Users can also customize these inside the advanced settings menu.
    val clientId: String
        get() = BuildConfig.GOOGLE_OAUTH_CLIENT_ID
        
    val clientSecret: String
        get() = BuildConfig.GOOGLE_OAUTH_CLIENT_SECRET

    var accessToken: String?
        get() = prefs.getString("access_token", null)
        set(value) = prefs.edit().putString("access_token", value).apply()

    var refreshToken: String?
        get() = prefs.getString("refresh_token", null)
        set(value) = prefs.edit().putString("refresh_token", value).apply()

    var userEmail: String?
        get() = prefs.getString("user_email", null)
        set(value) = prefs.edit().putString("user_email", value).apply()

    var userName: String?
        get() = prefs.getString("user_name", null)
        set(value) = prefs.edit().putString("user_name", value).apply()

    val isConnected: Boolean
        get() = accessToken != null || refreshToken != null

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val oauthApi = Retrofit.Builder()
        .baseUrl("https://oauth2.googleapis.com/")
        .client(okHttpClient)
        .build()
        .create(GoogleOAuthApi::class.java)

    private val driveApi = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/drive/v3/")
        .client(okHttpClient)
        .build()
        .create(GoogleDriveApi::class.java)

    fun getAuthUrl(): String {
        val redirectUri = "http://localhost/callback"
        val scope = "https://www.googleapis.com/auth/drive.file https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile"
        return "https://accounts.google.com/o/oauth2/v2/auth" +
                "?client_id=$clientId" +
                "&redirect_uri=$redirectUri" +
                "&response_type=code" +
                "&scope=${java.net.URLEncoder.encode(scope, "UTF-8")}" +
                "&access_type=offline" +
                "&prompt=consent"
    }

    suspend fun handleAuthCode(code: String): Pair<Boolean, String> {
        try {
            val redirectUri = "http://localhost/callback"
            val response = oauthApi.exchangeCode(code, clientId, clientSecret, redirectUri)
            val responseString = response.string()
            val json = JSONObject(responseString)
            
            if (json.has("error")) {
                val errorMsg = json.optString("error_description", json.optString("error"))
                Log.e(TAG, "OAuth exchange error: $errorMsg")
                return Pair(false, "OAuth Error: $errorMsg")
            }
            
            val access = json.optString("access_token")
            val refresh = json.optString("refresh_token")
            
            if (access.isNotEmpty()) {
                accessToken = access
                if (refresh.isNotEmpty()) {
                    refreshToken = refresh
                }
                fetchUserInfo()
                return Pair(true, "")
            }
            return Pair(false, "No access token received.")
        } catch (e: Exception) {
            Log.e(TAG, "OAuth exchange failed", e)
            return Pair(false, e.localizedMessage ?: "Unknown network error")
        }
    }

    suspend fun refreshAccessToken(): Boolean {
        val refresh = refreshToken ?: return false
        try {
            val response = oauthApi.refreshToken(refresh, clientId, clientSecret)
            val json = JSONObject(response.string())
            val access = json.optString("access_token")
            if (access.isNotEmpty()) {
                accessToken = access
                return true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Access token refresh failed", e)
        }
        return false
    }

    private suspend fun fetchUserInfo() {
        val token = accessToken ?: return
        try {
            val response = driveApi.getUserInfo("Bearer $token")
            val json = JSONObject(response.string())
            userEmail = json.optString("email", "Google User")
            userName = json.optString("name", "Connected")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch user info", e)
        }
    }

    fun disconnect() {
        prefs.edit().clear().apply()
    }

    private suspend fun <T> runWithRetry(block: suspend (String) -> T): T {
        var token = accessToken ?: throw Exception("Not connected to Google Drive")
        try {
            return block("Bearer $token")
        } catch (e: Exception) {
            Log.d(TAG, "API call failed, attempting token refresh", e)
            if (refreshAccessToken()) {
                token = accessToken ?: throw Exception("Not connected to Google Drive")
                return block("Bearer $token")
            } else {
                throw e
            }
        }
    }

    suspend fun uploadBackup(backupFile: File): Boolean {
        return runWithRetry { authHeader ->
            // 1. Delete existing backups if they exist to save space and keep it clean
            try {
                val query = "name = 'calculator_vault_backup.zip' and trashed = false"
                val listResponse = driveApi.listFiles(authHeader, query)
                val json = JSONObject(listResponse.string())
                val files = json.getJSONArray("files")
                for (i in 0 until files.length()) {
                    val fileObj = files.getJSONObject(i)
                    val id = fileObj.getString("id")
                    driveApi.deleteFile(authHeader, id)
                    Log.d(TAG, "Deleted old backup file with ID: $id")
                }
            } catch (e: Exception) {
                Log.w(TAG, "Error checking/deleting old backups", e)
            }

            // 2. Upload file multipart
            val metadataPart = MultipartBody.Part.createFormData(
                "metadata",
                "metadata.json",
                "{\"name\": \"calculator_vault_backup.zip\"}".toRequestBody("application/json; charset=UTF-8".toMediaType())
            )

            val fileRequestBody = backupFile.asRequestBody("application/zip".toMediaType())
            val filePart = MultipartBody.Part.createFormData("file", backupFile.name, fileRequestBody)

            val response = driveApi.uploadFileMultipart(authHeader, metadataPart, filePart)
            val responseStr = response.string()
            Log.d(TAG, "Upload response: $responseStr")
            
            val uploadJson = JSONObject(responseStr)
            uploadJson.has("id")
        }
    }

    suspend fun downloadBackup(destFile: File): Boolean {
        return runWithRetry { authHeader ->
            // 1. Find backup file
            val query = "name = 'calculator_vault_backup.zip' and trashed = false"
            val listResponse = driveApi.listFiles(authHeader, query)
            val json = JSONObject(listResponse.string())
            val files = json.getJSONArray("files")
            if (files.length() == 0) {
                throw Exception("No backup file found in your Google Drive.")
            }

            val fileId = files.getJSONObject(0).getString("id")

            // 2. Download media
            val downloadResponse = driveApi.downloadFile(authHeader, fileId)
            val inputStream = downloadResponse.byteStream()
            
            destFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            true
        }
    }

    suspend fun getBackupInfo(): BackupMetadata? {
        if (!isConnected) return null
        return try {
            runWithRetry { authHeader ->
                val query = "name = 'calculator_vault_backup.zip' and trashed = false"
                val listResponse = driveApi.listFiles(authHeader, query)
                val json = JSONObject(listResponse.string())
                val files = json.getJSONArray("files")
                if (files.length() > 0) {
                    val fileObj = files.getJSONObject(0)
                    BackupMetadata(
                        id = fileObj.getString("id"),
                        name = fileObj.getString("name"),
                        size = fileObj.optLong("size", 0L),
                        createdTime = fileObj.optString("createdTime", "Unknown")
                    )
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch backup metadata", e)
            null
        }
    }
}

data class BackupMetadata(
    val id: String,
    val name: String,
    val size: Long,
    val createdTime: String
)
