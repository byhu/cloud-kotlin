package poc.inbound

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class InboundService(val repo: TradeRepository, val kafkaTemplate: KafkaTemplate<String, String>) {

    @KafkaListener(id = "inbound", topics = ["inbound"], containerFactory = "listenerContainerFactory")
    @Transactional("chainedTransactionManager")
    fun inboundListener(record: ConsumerRecord<String, String>) {
        val jsonMapper = ObjectMapper().apply {
            registerKotlinModule()
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            setDateFormat(StdDateFormat())
        }
        val saved = repo.save(jsonMapper.readValue(record.value(), Trade::class.java))
        kafkaTemplate.send("pending", record.key(), jsonMapper.writeValueAsString(saved))
    }

    @KafkaListener(id = "pending", topics = ["inbound"], containerFactory = "listenerContainerFactory")
    fun pendingListener(record: ConsumerRecord<String, String>) {
        val jsonMapper = ObjectMapper().apply {
            registerKotlinModule()
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            dateFormat = StdDateFormat()
        }
        val saved = repo.save(jsonMapper.readValue(record.value(), Trade::class.java))
        kafkaTemplate.send("pending", record.key(), jsonMapper.writeValueAsString(saved))
    }
}