package akuchars

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class SenderApplicationRunner

fun main(args: Array<String>) {
    runApplication<SenderApplicationRunner>(*args)
}