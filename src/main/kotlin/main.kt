import parser.Parser
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val parser = Parser()
    val time = measureTimeMillis {
        parser.getAllSpecialities("/vuz/3935")
            .forEach { println(it) }
    }
    print(time)

}