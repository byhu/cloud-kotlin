package poc.settle

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
    fun containerFactory() =
            ConcurrentKafkaListenerContainerFactory<String, String>().apply {
                consumerFactory =
                        DefaultKafkaConsumerFactory<String, String>(mapOf(
                                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to kafkaProperties.bootstrapServers,
                                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                                ConsumerConfig.GROUP_ID_CONFIG to "test",
                                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to false,
                                ConsumerConfig.ISOLATION_LEVEL_CONFIG to "read_committed"))
            }
}