package poc.inbound

import com.mongodb.MongoClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDbFactory
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.data.mongodb.config.AbstractMongoConfiguration
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories


@Configuration
@EnableMongoRepositories
class MongoConfig : AbstractMongoConfiguration() {
    override fun mongoClient(): MongoClient = MongoClient()

    override fun getDatabaseName(): String = "bhu"

//    @Bean
//    fun mongoTxManager(dbFactory: MongoDbFactory): MongoTransactionManager {
//        return MongoTransactionManager(dbFactory)
//    }
}
