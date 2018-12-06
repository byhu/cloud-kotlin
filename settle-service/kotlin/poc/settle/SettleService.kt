package poc.settle

import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.http.MediaType
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Service
class SettleService(val repo: TransactionRepository) {

    @KafkaListener(id = "1settle2", topics = ["settle"], groupId = "1test", containerFactory = "containerFactory")
    fun settle2Listener(record: ConsumerRecord<String, String>) {
        val rtx = jsonMapper.readValue<RawTxn>(record.value())
        val stx = jsonMapper.readValue<SourceTxn>(rtx.txn)

        val sm = WebClient.create("http://localhost:9011")
                .get().uri("/findById/${stx.cusip}")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono<Security>()

        val pm = WebClient.create("http://localhost:9012")
                .post().uri("/allocate")
                .accept(MediaType.APPLICATION_JSON)
                .body(
                        Mono.just(Transaction(rtx.id, stx.amount, null)), Transaction::class.java
                )
                .retrieve()
                .bodyToMono<List<Piece>>()

        sm.zipWith(pm).map {
            Transaction(rtx.id, stx.amount, it.t1, it.t2)
        }.subscribe{
            repo.save(it).subscribe()
        }
    }
}

