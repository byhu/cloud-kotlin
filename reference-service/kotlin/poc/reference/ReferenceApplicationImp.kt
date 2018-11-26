package poc.reference

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class ReferenceApplication {

    @Bean
    fun init(repository: SecurityRepository) = CommandLineRunner {
        repository.deleteAll()
        repository.save(Security("aa", "Bauer"))
        repository.save(Security("bb", "O'Brian"))
        repository.save(Security("cc", "Bauer"))
    }
}

fun main(args: Array<String>) {
    runApplication<ReferenceApplication>(*args)
}

@RestController
class ReferenceController(val repository: SecurityRepository) {
    @GetMapping("/findById/{cusip}")
    fun findById(@PathVariable cusip: String) = repository.findById(cusip)
}