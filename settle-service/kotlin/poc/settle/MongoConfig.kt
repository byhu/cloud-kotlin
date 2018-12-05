package poc.settle

import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDbFactory
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories


@Configuration
@EnableMongoRepositories
class MongoConfig : AbstractReactiveMongoConfiguration() {
    override fun reactiveMongoClient(): MongoClient = MongoClients.create()

    override fun getDatabaseName(): String = "bhu"

    @Bean
    fun mongoTxManager(dbFactory: MongoDbFactory): MongoTransactionManager {
        return MongoTransactionManager(dbFactory)
    }
}
