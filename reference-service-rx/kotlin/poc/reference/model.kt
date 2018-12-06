package poc.reference

import org.springframework.data.repository.reactive.ReactiveCrudRepository

data class Security(val id: String, val desc: String)

interface SecurityRepository : ReactiveCrudRepository<Security, String>