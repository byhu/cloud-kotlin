package poc.enrich

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment


@EnableEurekaServer
@SpringBootApplication
class EurekaServiceApplication {
    @Autowired
    private val env: Environment? = null
}

fun main(args: Array<String>) {
    runApplication<EurekaServiceApplication>(*args)
}
