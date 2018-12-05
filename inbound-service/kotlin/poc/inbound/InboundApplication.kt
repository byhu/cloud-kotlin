package poc.inbound

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class InboundApplication {
    @Bean
    fun init() = CommandLineRunner {
//        Thread.currentThread().join()
    }
}

fun main(args: Array<String>) {
    runApplication<InboundApplication>(*args)
}
