package poc.inbound

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.transaction.ChainedTransactionManager
import org.springframework.kafka.transaction.KafkaTransactionManager
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.transaction.support.AbstractPlatformTransactionManager
import org.springframework.data.mongodb.MongoDbFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory


@SpringBootApplication
class InboundApplication(kafkaProperties: KafkaProperties) {

    @get:Bean
    val consumerProps: Map<String, Any> =
            mapOf(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to (kafkaProperties.bootstrapServers ?: ""),
                    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class,
                    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class,
                    ConsumerConfig.GROUP_ID_CONFIG to (kafkaProperties.consumer?.groupId ?: ""))

    @get:Bean
    val producerProps: Map<String, Any> =
            mapOf(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to (kafkaProperties.bootstrapServers ?: ""),
                    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class,
                    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class,
                    ConsumerConfig.GROUP_ID_CONFIG to (kafkaProperties.consumer?.groupId ?: ""))

    @Bean
    fun kafkaTransactionManager(): KafkaTransactionManager<*, *> {
        val ktm = KafkaTransactionManager(DefaultKafkaProducerFactory<String, String>(producerProps))
        ktm.transactionSynchronization = AbstractPlatformTransactionManager.SYNCHRONIZATION_ON_ACTUAL_TRANSACTION
        return ktm
    }

    @Bean
    fun transactionManager(dbFactory: MongoDbFactory): MongoTransactionManager = MongoTransactionManager(dbFactory)

    @Bean(name = arrayOf("chainedTransactionManager"))
    fun chainedTransactionManager(mtm: MongoTransactionManager,  ktm: KafkaTransactionManager<*, *>): ChainedTransactionManager =
            ChainedTransactionManager(ktm, mtm)
}

fun main(args: Array<String>) {
    runApplication<InboundApplication>(*args)
}

