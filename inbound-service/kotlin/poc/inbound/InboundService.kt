package poc.inbound

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.bson.types.ObjectId
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@ConditionalOnProperty(prefix = "poc", name = ["kafka.enabled"])
class InboundService(val repo: TransactionRepository, val kafkaTemplate: KafkaTemplate<String, String>) {

    @KafkaListener(id = "inbound", topics = ["inbound"], containerFactory = "containerFactory")
    @Transactional("chainedTransactionManager")
    fun inboundListener(record: ConsumerRecord<String, String>) {
        val jsonMapper = ObjectMapper().apply {
            registerKotlinModule()
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            setDateFormat(StdDateFormat())
        }

        val saved = repo.save(RawTxn(ObjectId.get().toString(), record.value()))

        kafkaTemplate.send("settle", record.key(), jsonMapper.writeValueAsString(saved))
    }
}