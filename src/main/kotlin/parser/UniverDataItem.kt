package parser

data class UniverDataItem(
    val url: String,
    val title: String,
    val cost: Int?,
    val minScoreSumToBudget: Int?,
    val budgetPlaces: Int?,
    val minScoreSumToPaid: Int?,
    val paidPlaces: Int?
)
