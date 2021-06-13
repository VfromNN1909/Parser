package parser

data class SpecialityDataItem(
    val url: String,
    val title: String,
    val studyForm: String,
    val exams: String,
    val cost: Int?,
    val minScoreSumToBudget: Int?,
    val budgetPlaces: Int?,
    val minScoreSumToPaid: Int?,
    val paidPlaces: Int?
)