import subprocess
result = subprocess.run(["gradle", ":app:compileDebugKotlin"], capture_output=True, text=True)
with open("compile_errors.txt", "w") as f:
    f.write(result.stdout)
    f.write(result.stderr)
