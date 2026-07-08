import re

content = open('app/src/main/java/com/example/CurrencyApi.kt').read()

content = content.replace('@Json(name = "rates")', '@Json(name = "conversion_rates")')
content = content.replace('@GET("v6/latest/{base}")', '@GET("latest/{base}")')
content = content.replace('baseUrl("https://open.er-api.com/")', 'baseUrl("https://v6.exchangerate-api.com/v6/867035d11e7902c727a903a3/")')

with open('app/src/main/java/com/example/CurrencyApi.kt', 'w') as f:
    f.write(content)
