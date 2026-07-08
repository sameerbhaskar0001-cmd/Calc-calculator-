val numericNameCounter = mutableMapOf<String, Int>()
val currentCounters = mutableMapOf<String, Int>()

fun getFriendlyName(type: String, id: String): String {
   val key = "$type-$id"
   if (!numericNameCounter.containsKey(key)) {
       val c = currentCounters.getOrDefault(type, 1)
       numericNameCounter[key] = c
       currentCounters[type] = c + 1
   }
   return "${type.replaceFirstChar { it.uppercase() }} ${numericNameCounter[key]}"
}
fun main() {
    println(getFriendlyName("photo", "1234"))
    println(getFriendlyName("photo", "5678"))
    println(getFriendlyName("video", "5678"))
}
