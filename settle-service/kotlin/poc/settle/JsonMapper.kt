package poc.settle

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.util.StdDateFormat
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

val jsonMapper = ObjectMapper().apply {
    registerKotlinModule()
    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    dateFormat = StdDateFormat()
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
}

