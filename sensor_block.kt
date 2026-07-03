    // Unified sensor detector for Panic Gesture (Shake and Face Down)
    if ((panicEnabled || screenDownLock) && vaultUnlocked) {
        val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
        val accelerometer = remember { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }

        DisposableEffect(sensorManager, accelerometer) {
            var lastUpdate = 0L
            var lastX = 0f
            var lastY = 0f
            var lastZ = 0f

            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    val curTime = System.currentTimeMillis()
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]

                    // 1. Screen Down Lock detection (Z-axis negative gravity)
                    if (screenDownLock && z < -8.5f) {
                        viewModel.triggerKeypressEffects(context)
                        viewModel.lockVault()
                        Toast.makeText(context, "Vault locked: Face down detected!", Toast.LENGTH_SHORT).show()
                        val homeIntent = Intent(Intent.ACTION_MAIN).apply {
                            addCategory(Intent.CATEGORY_HOME)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                        context.startActivity(homeIntent)
                        return
                    }

                    // 2. Shake detection
                    if (panicEnabled && (curTime - lastUpdate) > 150) {
                        val diffTime = (curTime - lastUpdate)
                        lastUpdate = curTime

                        val speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000

                        if (speed > 1000) { // Shake detected
                            viewModel.triggerKeypressEffects(context)
                            viewModel.lockVault()
                            if (panicAction == "lock") {
                                Toast.makeText(context, "Vault locked via shake gesture!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Emergency lock initiated!", Toast.LENGTH_SHORT).show()
                                val homeIntent = Intent(Intent.ACTION_MAIN).apply {
                                    addCategory(Intent.CATEGORY_HOME)
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                                context.startActivity(homeIntent)
                            }
                        }

                        lastX = x
                        lastY = y
                        lastZ = z
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
            }

            if (accelerometer != null) {
                sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
            }
            onDispose {
                sensorManager.unregisterListener(listener)
            }
        }
    }
