package poc.settle

import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.bson.types.ObjectId
import org.springframework.http.MediaType
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono

class SettleService(val repo: TransactionRepository) {

    @KafkaListener(id = "pending", topics = ["settle"], containerFactory = "containerFactory")
    fun settleListener(record: ConsumerRecord<String, String>) {
        val rtx = jsonMapper.readValue<RawTxn>(record.value())
        val stx = jsonMapper.readValue<SourceTxn>(rtx.txn).apply { id = rtx.id }

        val t = runBlocking {
            val sm = async {
                WebClient.create("http://localhost:8080")
                        .get().uri("/cusip/${stx.cusip}")
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono<Security>().block()
            }
            val pm = async {
                WebClient.create("http://localhost:8080")
                        .put().uri("/allocate")
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono<List<Piece>>().block()
            }

            Transaction(stx.id, sm.await(), pm.await() ?: listOf())
        }
        repo.save(t)
    }
}

