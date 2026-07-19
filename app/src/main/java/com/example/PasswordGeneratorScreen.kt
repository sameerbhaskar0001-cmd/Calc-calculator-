package com.example

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalAppThemeColors
import java.security.SecureRandom

enum class PasswordStrength(val label: String, val color: Color, val score: Int) {
    WEAK("Weak", Color(0xFFEF5350), 1),
    MEDIUM("Medium", Color(0xFFFFB74D), 2),
    STRONG("Strong", Color(0xFF66BB6A), 3)
}

fun generatePassword(
    length: Int,
    includeUppercase: Boolean,
    includeLowercase: Boolean,
    includeNumbers: Boolean,
    includeSymbols: Boolean,
    excludeSimilar: Boolean
): String {
    val uppercaseCharsFull = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val lowercaseCharsFull = "abcdefghijklmnopqrstuvwxyz"
    val numberCharsFull = "0123456789"
    val symbolCharsFull = "!@#$%^&*()_+-=[]{}|;:,.<>?/\"'\\`~"

    fun String.filterSimilar(): String {
        val similar = setOf('I', 'l', '1', 'O', '0', 'o', 'S', '5', 'G', '6', 'B', '8', 'Z', '2', 't', 'f', 'i', 's')
        return this.filter { it !in similar }
    }

    val finalUpper = if (excludeSimilar) uppercaseCharsFull.filterSimilar() else uppercaseCharsFull
    val finalLower = if (excludeSimilar) lowercaseCharsFull.filterSimilar() else lowercaseCharsFull
    val finalNumbers = if (excludeSimilar) numberCharsFull.filterSimilar() else numberCharsFull
    val finalSymbols = if (excludeSimilar) symbolCharsFull.filterSimilar() else symbolCharsFull

    val pool = StringBuilder()
    val guaranteed = mutableListOf<Char>()
    val random = SecureRandom()

    if (includeUppercase && finalUpper.isNotEmpty()) {
        pool.append(finalUpper)
        guaranteed.add(finalUpper[random.nextInt(finalUpper.length)])
    }
    if (includeLowercase && finalLower.isNotEmpty()) {
        pool.append(finalLower)
        guaranteed.add(finalLower[random.nextInt(finalLower.length)])
    }
    if (includeNumbers && finalNumbers.isNotEmpty()) {
        pool.append(finalNumbers)
        guaranteed.add(finalNumbers[random.nextInt(finalNumbers.length)])
    }
    if (includeSymbols && finalSymbols.isNotEmpty()) {
        pool.append(finalSymbols)
        guaranteed.add(finalSymbols[random.nextInt(finalSymbols.length)])
    }

    if (pool.isEmpty()) {
        return ""
    }

    val password = StringBuilder()
    password.append(guaranteed.joinToString(""))

    val remaining = length - password.length
    for (i in 0 until remaining) {
        password.append(pool[random.nextInt(pool.length)])
    }

    val list = password.toList().shuffled(random)
    return list.joinToString("")
}

fun evaluateStrength(
    password: String,
    length: Int,
    includeUppercase: Boolean,
    includeLowercase: Boolean,
    includeNumbers: Boolean,
    includeSymbols: Boolean
): PasswordStrength {
    if (password.isEmpty()) return PasswordStrength.WEAK
    
    var score = 0
    if (length >= 14) score += 2
    else if (length >= 10) score += 1
    
    var typesCount = 0
    if (includeUppercase) typesCount++
    if (includeLowercase) typesCount++
    if (includeNumbers) typesCount++
    if (includeSymbols) typesCount++
    
    score += typesCount
    
    return when {
        score <= 3 || length < 8 -> PasswordStrength.WEAK
        score <= 4 -> PasswordStrength.MEDIUM
        else -> PasswordStrength.STRONG
    }
}

@Composable
fun PasswordGeneratorScreen(
    onBack: () -> Unit
) {
    val themeColors = LocalAppThemeColors.current
    val ThemePurple = themeColors.themePurple
    val TextMedium = themeColors.textMedium
    val clipboardManager = LocalClipboardManager.current

    var passwordLength by remember { mutableStateOf(16) }
    var includeUppercase by remember { mutableStateOf(true) }
    var includeLowercase by remember { mutableStateOf(true) }
    var includeNumbers by remember { mutableStateOf(true) }
    var includeSymbols by remember { mutableStateOf(true) }
    var excludeSimilar by remember { mutableStateOf(false) }

    var generatedPassword by remember { mutableStateOf("") }
    var showCopiedToast by remember { mutableStateOf(false) }

    LaunchedEffect(passwordLength, includeUppercase, includeLowercase, includeNumbers, includeSymbols, excludeSimilar) {
        val isValid = includeUppercase || includeLowercase || includeNumbers || includeSymbols
        if (isValid) {
            generatedPassword = generatePassword(
                length = passwordLength,
                includeUppercase = includeUppercase,
                includeLowercase = includeLowercase,
                includeNumbers = includeNumbers,
                includeSymbols = includeSymbols,
                excludeSimilar = excludeSimilar
            )
        } else {
            generatedPassword = ""
        }
    }

    val isValidConfig = includeUppercase || includeLowercase || includeNumbers || includeSymbols

    val contentColorOnPurple = remember(ThemePurple) {
        val r = ThemePurple.red
        val g = ThemePurple.green
        val b = ThemePurple.blue
        val luminance = 0.2126 * r + 0.7152 * g + 0.0722 * b
        if (luminance > 0.6) Color(0xFF111424) else Color.White
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                
                Text(
                    text = "Password Generator",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                UnifiedGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
                    elevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(ThemePurple.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VpnKey,
                                    contentDescription = null,
                                    tint = ThemePurple,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Password Generator",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Secure & Offline",
                                    fontSize = 12.sp,
                                    color = Color.White.copy(alpha = 0.5f)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(2.dp))
                        
                        Text(
                            text = "Generate strong, secure passwords locally on your device. Your keys are never saved or sent online.",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.6f),
                            lineHeight = 18.sp
                        )
                    }
                }

                UnifiedGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
                    elevation = 3.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "GENERATED PASSWORD",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = ThemePurple
                            )
                            
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = Color(0xFF00E676),
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "100% Local",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF00E676)
                                )
                            }
                        }
                        
                        Row(
                            modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color.Black.copy(alpha = 0.4f))
                                        .padding(horizontal = 14.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = generatedPassword.ifEmpty { "Select at least one option" },
                                color = if (generatedPassword.isEmpty()) Color.White.copy(alpha = 0.3f) else Color.White,
                                fontSize = if (generatedPassword.length > 24) 14.sp else 18.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f),
                                maxLines = 2
                            )
                            
                            if (generatedPassword.isNotEmpty()) {
                                IconButton(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(generatedPassword))
                                        showCopiedToast = true
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy Password",
                                        tint = Color.White.copy(alpha = 0.7f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                
                                IconButton(
                                    onClick = {
                                        if (isValidConfig) {
                                            generatedPassword = generatePassword(
                                                length = passwordLength,
                                                includeUppercase = includeUppercase,
                                                includeLowercase = includeLowercase,
                                                includeNumbers = includeNumbers,
                                                includeSymbols = includeSymbols,
                                                excludeSimilar = excludeSimilar
                                            )
                                        }
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Regenerate",
                                        tint = ThemePurple,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        if (generatedPassword.isNotEmpty()) {
                            val strength = evaluateStrength(
                                generatedPassword,
                                passwordLength,
                                includeUppercase,
                                includeLowercase,
                                includeNumbers,
                                includeSymbols
                            )
                            
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Strength: ${strength.label}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = strength.color
                                    )
                                    
                                    val entropy = (passwordLength * when {
                                        includeUppercase && includeLowercase && includeNumbers && includeSymbols -> 6.5
                                        includeUppercase && includeLowercase && includeNumbers -> 5.7
                                        else -> 4.5
                                    }).toInt()
                                    Text(
                                        text = "~$entropy bits entropy",
                                        fontSize = 11.sp,
                                        color = Color.White.copy(alpha = 0.4f)
                                    )
                                }
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    val activeSegments = strength.score
                                    for (i in 1..3) {
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(6.dp)
                                                .clip(RoundedCornerShape(3.dp))
                                                .background(
                                                    if (i <= activeSegments) strength.color 
                                                    else Color.White.copy(alpha = 0.08f)
                                                )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Text(
                    text = "PASSWORD CRITERIA",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMedium,
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 2.dp)
                )

                UnifiedGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
                    elevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Password Length",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Min: 6 | Max: 64 characters",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.5f)
                                )
                            }
                            
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(ThemePurple.copy(alpha = 0.15f))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = passwordLength.toString(),
                                    color = ThemePurple,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                        
                        Slider(
                            value = passwordLength.toFloat(),
                            onValueChange = { passwordLength = it.toInt() },
                            valueRange = 6f..64f,
                            colors = SliderDefaults.colors(
                                thumbColor = Color.White,
                                activeTrackColor = ThemePurple,
                                inactiveTrackColor = Color.White.copy(alpha = 0.12f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                UnifiedGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    bgColor = Color(0xFF1B2031).copy(alpha = 0.95f),
                    elevation = 2.dp
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        @Composable
                        fun OptionRow(
                            title: String,
                            description: String,
                            checked: Boolean,
                            onCheckedChange: (Boolean) -> Unit,
                            icon: androidx.compose.ui.graphics.vector.ImageVector,
                            iconBg: Color,
                            iconTint: Color,
                            enabled: Boolean = true
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(enabled = enabled) { onCheckedChange(!checked) }
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(iconBg.copy(alpha = 0.12f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = icon,
                                            contentDescription = null,
                                            tint = iconTint,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                    Column {
                                        Text(
                                            text = title,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (enabled) Color.White else Color.White.copy(alpha = 0.4f)
                                        )
                                        Text(
                                            text = description,
                                            fontSize = 11.sp,
                                            color = Color.White.copy(alpha = 0.5f),
                                            lineHeight = 14.sp
                                        )
                                    }
                                }
                                
                                Switch(
                                    checked = checked,
                                    onCheckedChange = if (enabled) onCheckedChange else null,
                                    enabled = enabled,
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = contentColorOnPurple,
                                        checkedTrackColor = ThemePurple,
                                        uncheckedThumbColor = Color.White.copy(alpha = 0.6f),
                                        uncheckedTrackColor = Color.White.copy(alpha = 0.12f)
                                    )
                                )
                            }
                        }

                        OptionRow(
                            title = "Uppercase Letters",
                            description = "Include capital letters (A-Z)",
                            checked = includeUppercase,
                            onCheckedChange = { includeUppercase = it },
                            icon = Icons.Default.TextFields,
                            iconBg = Color(0xFF0EA5E9),
                            iconTint = Color(0xFF0EA5E9)
                        )
                        
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.05f)))

                        OptionRow(
                            title = "Lowercase Letters",
                            description = "Include small letters (a-z)",
                            checked = includeLowercase,
                            onCheckedChange = { includeLowercase = it },
                            icon = Icons.Default.Title,
                            iconBg = Color(0xFF3B82F6),
                            iconTint = Color(0xFF3B82F6)
                        )

                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.05f)))

                        OptionRow(
                            title = "Include Numbers",
                            description = "Include numeric digits (0-9)",
                            checked = includeNumbers,
                            onCheckedChange = { includeNumbers = it },
                            icon = Icons.Default.Numbers,
                            iconBg = Color(0xFF00E676),
                            iconTint = Color(0xFF00E676)
                        )

                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.05f)))

                        OptionRow(
                            title = "Include Symbols",
                            description = "Include special characters (!@#$%)",
                            checked = includeSymbols,
                            onCheckedChange = { includeSymbols = it },
                            icon = Icons.Default.AlternateEmail,
                            iconBg = Color(0xFFFF9100),
                            iconTint = Color(0xFFFF9100)
                        )

                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.05f)))

                        OptionRow(
                            title = "Exclude Similar Characters",
                            description = "Avoid confusion (e.g. O, 0, I, l)",
                            checked = excludeSimilar,
                            onCheckedChange = { excludeSimilar = it },
                            icon = Icons.Default.Difference,
                            iconBg = Color(0xFFEF5350),
                            iconTint = Color(0xFFEF5350)
                        )
                    }
                }

                Button(
                    onClick = {
                        if (isValidConfig) {
                            generatedPassword = generatePassword(
                                length = passwordLength,
                                includeUppercase = includeUppercase,
                                includeLowercase = includeLowercase,
                                includeNumbers = includeNumbers,
                                includeSymbols = includeSymbols,
                                excludeSimilar = excludeSimilar
                            )
                        }
                    },
                    enabled = isValidConfig,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ThemePurple,
                        contentColor = contentColorOnPurple,
                        disabledContainerColor = Color.White.copy(alpha = 0.08f),
                        disabledContentColor = Color.White.copy(alpha = 0.3f)
                    )
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.VpnKey,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Generate Password",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        
        AnimatedVisibility(
            visible = showCopiedToast,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it }),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        ) {
            LaunchedEffect(showCopiedToast) {
                if (showCopiedToast) {
                    kotlinx.coroutines.delay(2000)
                    showCopiedToast = false
                }
            }
            UnifiedGlassCard(
                shape = RoundedCornerShape(12.dp),
                bgColor = Color(0xFF00E676).copy(alpha = 0.9f),
                elevation = 6.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Password copied to clipboard!",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
