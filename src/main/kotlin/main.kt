import parser.Parser
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val parser = Parser()
    val time = measureTimeMillis {
        parser.getAllPrograms("/vuz/3935/")
            .forEach { println(it) }
    }
    print(time)

}