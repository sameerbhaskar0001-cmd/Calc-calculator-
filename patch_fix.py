lines = open('app/src/main/java/com/example/CalculatorViewModel.kt').read().splitlines()
idx = lines.index('                    mediaStoreUris.add(uri)')
# the next line should be '                }'
# the next line should be '            }'
# but we have an extra '                }'
if lines[idx+2].strip() == '}':
    lines.pop(idx+2)
with open('app/src/main/java/com/example/CalculatorViewModel.kt', 'w') as f:
    f.write('\n'.join(lines) + '\n')
