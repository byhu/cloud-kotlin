package poc.inbound

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.data.transaction.ChainedTransactionManager
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.transaction.KafkaTransactionManager
import org.springframework.transaction.support.AbstractPlatformTransactionManager

@Configuration
@EnableKafka
class KafkaConfig {
    @Bean
    fun producerFactory(kafkaProperties: KafkaProperties) =
            DefaultKafkaProducerFactory<String, String>(mapOf(
                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers,
                    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringDeserializer::class,
                    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringDeserializer::class))

    @Bean
    fun kafkaTemplate(producerFactory: ProducerFactory<String, String>) = KafkaTemplate(producerFactory)

    @Bean
    fun consumerFactory(kafkaProperties: KafkaProperties) =
            DefaultKafkaConsumerFactory<String, String>(mapOf(
                    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers,
                    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class,
                    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class,
                    ConsumerConfig.GROUP_ID_CONFIG to kafkaProperties.consumer.groupId))

    @Bean
    fun listenerContainerFactory(cf: ConsumerFactory<String, String>) =
            ConcurrentKafkaListenerContainerFactory<String, String>().apply {
                consumerFactory = cf
            }

    @Bean
    fun kafkaTransactionManager(pf: ProducerFactory<String, String>): KafkaTransactionManager<*, *> =
            KafkaTransactionManager(pf).apply {
                transactionSynchronization = AbstractPlatformTransactionManager.SYNCHRONIZATION_ON_ACTUAL_TRANSACTION
            }

    @Bean
    fun chainedTransactionManager(mtm: MongoTransactionManager, ktm: KafkaTransactionManager<*, *>) =
            ChainedTransactionManager(ktm, mtm)
}