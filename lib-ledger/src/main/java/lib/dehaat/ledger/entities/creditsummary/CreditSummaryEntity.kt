package lib.dehaat.ledger.entities.creditsummary

data class CreditSummaryEntity(
    val credit: CreditEntity,
    val overdue: OverdueEntity,
    val info: InfoEntity
)
