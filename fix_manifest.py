with open("app/src/main/AndroidManifest.xml", "r") as f:
    content = f.read()

content = content.replace(
    '<provider android:name="androidx.startup.InitializationProvider" android:authorities="${applicationId}.androidx-startup" android:exported="false" tools:node="merge"><meta-data android:name="androidx.camera.core.CameraXConfig.Provider" android:value="androidx.startup" tools:node="remove" /></provider>',
    ''
)

with open("app/src/main/AndroidManifest.xml", "w") as f:
    f.write(content)
