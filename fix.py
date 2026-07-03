import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

# Fix VaultTabLockedContent
old_locked = """                                                "Unlock" -> {
                                                    if (viewModel.tryUnlockVault(pinInput)) {
                                                        pinInput = ""
}"""

new_locked = """                                                "Unlock" -> {
                                                    if (viewModel.tryUnlockVault(pinInput)) {
                                                        pinInput = ""
                                                    } else {
                                                        pinError = true
                                                    }
                                                }
                                                else -> {
                                                    if (pinInput.length < 8) pinInput += key
                                                }
                                            }
                                        }
                                ) {
                                    Text(
                                        text = key,
                                        color = contentColor,
                                        fontSize = if (isSpecial) 16.sp else 24.sp,
                                        fontWeight = if (isSpecial) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}"""

content = content.replace(old_locked, new_locked)

# Fix VaultTabUnlockedContent
# We need to remove the trailing `    } else {` that was accidentally included.
# Actually, the sensor block got copied.
# Let's find:
#                         )
#                     }
#                 }
#             }
#         }
#     } else {
#         // Vault Unlocked Content: Advanced Private Media Hub
# 
# Wait, let's just do a regex substitution.

bad_else = """                        )
                    }
                }
            }
        }
    } else {
        // Vault Unlocked Content: Advanced Private Media Hub"""

good_else = """        // Vault Unlocked Content: Advanced Private Media Hub"""

content = content.replace(bad_else, good_else)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
