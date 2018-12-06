package poc.settle

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import java.math.BigDecimal
import java.sql.Timestamp

data class RawTxn(val id: String, val txn: String, val receivedAt: Timestamp = Timestamp(System.currentTimeMillis()))

data class SourceTxn(val cusip: String, val amount: BigDecimal, val receiveAt: Timestamp = Timestamp(System.currentTimeMillis()))

data class Piece(val id: Int, val tid: String, val cusip: String, val amount: BigDecimal)

data class Security(val cusip: String, val desc: String)

data class Transaction(val id: String,
                       val security: Security?,
                       val pieces: List<Piece> = listOf(),
                       val receiveAt: Timestamp = Timestamp(System.currentTimeMillis()))


interface TransactionRepository : ReactiveCrudRepository<Transaction, String>