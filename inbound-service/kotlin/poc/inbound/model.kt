package poc.inbound

import org.springframework.data.repository.CrudRepository
import java.sql.Timestamp


data class RawTxn(val id: String, val txn: String, val receivedAt: Timestamp = Timestamp(System.currentTimeMillis()))

interface TransactionRepository : CrudRepository<RawTxn, String>
