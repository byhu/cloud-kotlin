package poc.inbound

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.data.transaction.ChainedTransactionManager
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.transaction.KafkaTransactionManager
import org.springframework.transaction.support.AbstractPlatformTransactionManager

@Configuration
@EnableKafka
class KafkaConfig(val kafkaProperties: KafkaProperties, val env: Environment) {

    @Bean
    fun producerFactory() =
            DefaultKafkaProducerFactory<String, String>(mapOf(
                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers,
                    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
                    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
                    ProducerConfig.CLIENT_ID_CONFIG to "inbound",
                    ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG to true,
                    ProducerConfig.TRANSACTIONAL_ID_CONFIG to "inbound")).apply { setTransactionIdPrefix("inbound") }

    @Bean
    fun kafkaTemplate(producerFactory: ProducerFactory<String, String>) = KafkaTemplate(producerFactory)

    @Bean
    fun containerFactory() =
            ConcurrentKafkaListenerContainerFactory<String, String>().apply {
                consumerFactory =
                        DefaultKafkaConsumerFactory<String, String>(mapOf(
                                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers,
                                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                                ConsumerConfig.GROUP_ID_CONFIG to kafkaProperties.consumer.groupId,
                                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to false,
                                ConsumerConfig.ISOLATION_LEVEL_CONFIG to "read_committed"))
            }

    @Bean
    fun kafkaTransactionManager(pf: ProducerFactory<String, String>): KafkaTransactionManager<String, String> =
            KafkaTransactionManager(pf).apply {
                transactionSynchronization = AbstractPlatformTransactionManager.SYNCHRONIZATION_ON_ACTUAL_TRANSACTION
            }

//    @Bean
//    fun chainedTransactionManager(mtm: MongoTransactionManager, ktm: KafkaTransactionManager<String, String>) =
//            ChainedTransactionManager(ktm, mtm)
}