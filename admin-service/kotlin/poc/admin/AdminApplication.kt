package poc.admin

import de.codecentric.boot.admin.server.config.EnableAdminServer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.context.annotation.Configuration

@SpringBootApplication
@EnableDiscoveryClient
@Configuration
@EnableAdminServer
class AdminApplication

fun main(args: Array<String>) {
    runApplication<AdminApplication>(*args)
}
