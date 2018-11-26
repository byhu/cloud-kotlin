package poc.reference

import org.springframework.data.annotation.Id
import org.springframework.data.repository.reactive.ReactiveCrudRepository

data class Security(@Id val cusip: String, val desc: String)

interface SecurityRepository : ReactiveCrudRepository<Security, String>