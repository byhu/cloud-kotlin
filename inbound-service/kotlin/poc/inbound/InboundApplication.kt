package poc.inbound

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InboundApplication {

}

fun main(args: Array<String>) {
    runApplication<InboundApplication>(*args)
}
