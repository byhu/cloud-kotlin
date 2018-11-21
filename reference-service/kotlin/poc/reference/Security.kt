package poc.reference

import org.springframework.data.annotation.Id
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.Repository

data class Security(@Id val cusip: String, val desc: String)

interface SecurityRepository : CrudRepository<Security, String>