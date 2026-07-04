import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

bad_radial = r"center = androidx\.compose\.ui\.geometry\.Offset\(x = Float\.POSITIVE_INFINITY, y = Float\.POSITIVE_INFINITY\), // this might not work easily\s*radius = 1000f"
good_radial = r"""center = androidx.compose.ui.geometry.Offset(x = 0f, y = 0f),
                    radius = 300f"""
content = re.sub(bad_radial, good_radial, content)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
