package com.example

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import android.util.Log

/**
 * High-performance, zero-latency retro sound manager using Android SoundPool.
 * Handles audio attributes, memory caching, error handling, and safe lifecycle release.
 */
class GameSoundEffects(private val context: Context) {

    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(5)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()

    private var jumpSoundId: Int = 0
    private var doubleJumpSoundId: Int = 0
    private var collectSoundId: Int = 0
    private var hitSoundId: Int = 0
    private var gameOverSoundId: Int = 0

    init {
        loadSounds()
    }

    private fun loadSounds() {
        try {
            jumpSoundId = soundPool.load(context, R.raw.runner_jump, 1)
            doubleJumpSoundId = soundPool.load(context, R.raw.runner_double_jump, 1)
            collectSoundId = soundPool.load(context, R.raw.runner_collect, 1)
            hitSoundId = soundPool.load(context, R.raw.runner_hit, 1)
            gameOverSoundId = soundPool.load(context, R.raw.runner_game_over, 1)
        } catch (e: Exception) {
            Log.e("GameSoundEffects", "Error loading runner sounds", e)
        }
    }

    fun playJump() {
        if (jumpSoundId != 0) {
            soundPool.play(jumpSoundId, 0.9f, 0.9f, 1, 0, 1.0f)
        }
    }

    fun playDoubleJump() {
        if (doubleJumpSoundId != 0) {
            soundPool.play(doubleJumpSoundId, 0.9f, 0.9f, 1, 0, 1.0f)
        }
    }

    fun playCollect() {
        if (collectSoundId != 0) {
            soundPool.play(collectSoundId, 0.95f, 0.95f, 1, 0, 1.0f)
        }
    }

    fun playHit() {
        if (hitSoundId != 0) {
            soundPool.play(hitSoundId, 1.0f, 1.0f, 2, 0, 1.0f)
        }
    }

    fun playGameOver() {
        if (gameOverSoundId != 0) {
            soundPool.play(gameOverSoundId, 1.0f, 1.0f, 2, 0, 1.0f)
        }
    }

    fun release() {
        try {
            soundPool.release()
        } catch (e: Exception) {
            Log.e("GameSoundEffects", "Error releasing SoundPool", e)
        }
    }
}
