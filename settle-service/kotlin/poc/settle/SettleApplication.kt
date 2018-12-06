package poc.settle

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query


@SpringBootApplication(exclude = [MongoReactiveDataAutoConfiguration::class])
class SettleApplication {
    @Bean
    fun init(template: ReactiveMongoTemplate) = CommandLineRunner {
//        template.find(Query.query(Criteria.where("receivedAt").lt("White")), Transaction.class.java);
    }
}

fun main(args: Array<String>) {
    runApplication<SettleApplication>(*args)
}
