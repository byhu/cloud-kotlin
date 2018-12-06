package poc.piece

import java.math.BigDecimal

data class Piece(val id: Int, val tid: String, val amount: BigDecimal)

data class Transaction(val id: String, val amount: BigDecimal, val pieces: List<Piece> = listOf())
