content = open('app/src/main/java/com/example/CalculatorViewModel.kt').read()
import re
content = re.sub(r'_apiStatus\.value = ApiStatus\.ERROR\s*}', '_apiStatus.value = ApiStatus.ERROR\n_lastUpdated.value = e.message ?: "Error"\n}', content)
with open('app/src/main/java/com/example/CalculatorViewModel.kt', 'w') as f:
    f.write(content)
