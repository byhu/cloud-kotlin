package poc.inbound


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(topics = ["inbound"], count = 3)
@RunWith(SpringRunner::class)
class Test {
    @Autowired
    lateinit var template: KafkaTemplate<String, String>

    @Test
    @Transactional("chainedTransactionManager")
    fun `send messages`() {
        val jsonMapper = ObjectMapper().apply {
            registerKotlinModule()
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            dateFormat = StdDateFormat()
        }

//        template.send("inbound", "a", jsonMapper.writeValueAsString(Trade(null, "a", "c1", 200.toBigDecimal())))
//        template.send("inbound", "a", jsonMapper.writeValueAsString(Trade(null, "b", "c1", 100.toBigDecimal())))
    }
}