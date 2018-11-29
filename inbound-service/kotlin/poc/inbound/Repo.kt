package poc.inbound

import org.springframework.data.repository.CrudRepository
import java.math.BigDecimal

data class Trade(val id: String? = null, val tradeId: String, val cusip: String, val amount: BigDecimal)

interface TradeRepository : CrudRepository<Trade, String>
