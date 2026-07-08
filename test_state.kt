import kotlin.reflect.KProperty

class BackStackDelegate(initial: String) {
    var backStack = listOf(initial)
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String = backStack.last()
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {}
}

fun test() {
    var a by BackStackDelegate("Home")
}
