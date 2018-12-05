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
@RunWith(SpringRunner::class)
class TestData {
    @Autowired
    lateinit var template: KafkaTemplate<String, String>

    @Test
    fun `send messages`() {
        template.executeInTransaction {
            val t1 = """{"cusip": "aa", "amount": 200 }"""
            val t2 = """{"cusip": "bb", "amount": 20 }"""
            template.send("inbound", "a", t1)
            template.send("inbound", "a", t1)
        }
    }
}