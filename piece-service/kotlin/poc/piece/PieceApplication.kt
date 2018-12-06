package poc.piece

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.BodyInserters.fromObject
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import org.springframework.web.reactive.function.server.router
import reactor.core.publisher.Mono

@SpringBootApplication
class PieceApplication

fun main(args: Array<String>) {
    runApplication<PieceApplication>(*args)
}

@RestController
class PieceController(val handler: PieceHandler) {
    @Bean
    fun routes() = router {
        POST("/allocate", handler::allocate)
    }
}

@Component
class PieceHandler {
    fun allocate(request: ServerRequest): Mono<ServerResponse> =
            request.bodyToMono<Transaction>().map {
                listOf(Piece(1, it.id, it.amount / 2.toBigDecimal()), Piece(2, it.id, it.amount / 2.toBigDecimal()))
            }.flatMap {
                ServerResponse.ok().body(fromObject(it))
            }
}
