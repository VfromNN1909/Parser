package parser

data class ProgramDataItem(
    val url: String,
    val title: String,
    val studyProgram: String,
    val exams: String,
    val cost: Int?,
    val minScoreSumToBudget: Int?,
    val budgetPlaces: Int?,
    val minScoreSumToPaid: Int?,
    val paidPlaces: Int?
)
