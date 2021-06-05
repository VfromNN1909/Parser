import parser.Parser
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val parser = Parser()
    val time = measureTimeMillis {
        parser.getUniversInAllPages().forEach {
            println(it)
        }
    }
    print(time)

}