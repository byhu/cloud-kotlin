package poc.reference

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.body
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Flux

@SpringBootApplication
class ReferenceApplication {

    @Bean
    fun init(repository: SecurityRepository) = CommandLineRunner {
        repository.deleteAll()
                .thenMany(Flux.just(Security("aa", "Bauer"), Security("bb", "O'Brian"), Security("cb", "Bauer")))
                .flatMap {
                    repository.save(it)
                }
                .subscribe()
    }
}

fun main(args: Array<String>) {
    runApplication<ReferenceApplication>(*args)
}

@RestController
class ReferenceController(val repo: SecurityRepository) {
    @get:Bean
    val routes = router {
        GET("/findById/{cusip}") {
            ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(
                            repo.findById(it.pathVariable("cusip"))
                    )
                    .switchIfEmpty(ServerResponse.notFound().build())
        }
    }
}