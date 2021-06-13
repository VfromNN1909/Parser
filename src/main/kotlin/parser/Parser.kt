package parser

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class Parser {
    companion object {
        const val BASE_URL_ALL_UNIVERSITIES = "https://vuzopedia.ru/region/city/67"
        const val BASE_URL_UNIVER = "https://vuzopedia.ru"
    }

    private val allUniversitiesInNNPage = Jsoup.connect(BASE_URL_ALL_UNIVERSITIES).get()

    fun getUniversInAllPages(): List<UniverDataItem> {
        val resList = mutableListOf<UniverDataItem>()
        for (i in 1..getPageCount()) {
            val doc = Jsoup.connect("$BASE_URL_ALL_UNIVERSITIES?page=$i").get()
            resList.addAll(getAllUniversInPage(doc))
        }
        return resList
    }

    private fun getAllUniversInPage(allUniversitiesInPage: Document): List<UniverDataItem> {
        return allUniversitiesInPage.select("div.vuzesfullnorm")
            .map { vuz -> getUniverInfo(vuz) }
    }


    private fun getUniverInfo(univerItem: Element): UniverDataItem {
        val budget: List<String?>?
        val paid: List<String?>?

        // title
        val title = univerItem.select("div.itemVuzTitle").text()

        // url
        val url = univerItem.select("div.col-md-7").select("a").attr("href")

        val univerNums = univerItem.select("div.col-md-5").select("div.col-md-4")

        // cost
        val cost = univerNums[0].select("center").select("center > a.tooltipq").first()
            .ownText().filter { it.isDigit() }


        // budget
        budget = if (univerNums[1].ownText() == "нет") null
        else univerNums[1].select("center > a.tooltipq")
            .map { element -> element.ownText().filter { it.isDigit() } }
            .map { if (it.isEmpty() or it.isBlank()) null else it }

        // paid
        paid = if (univerNums[2].ownText() == "нет") null
        else univerNums[2].select("center > a.tooltipq")
            .map { element -> element.ownText().filter { it.isDigit() } }
            .map { if (it.isEmpty() or it.isBlank()) null else it }


        return UniverDataItem(
            url,
            title,
            if (cost == "") null else cost.toInt(),
            if (budget?.get(0) == "" || budget?.get(0) == null) null else budget[0]?.toInt(),
            if (budget?.get(1) == "" || budget?.get(1) == null) null else budget[1]?.toInt(),
            if (paid?.get(0) == "" || paid?.get(0) == null) null else paid[0]?.toInt(),
            if (paid?.get(1) == "" || paid?.get(1) == null) null else paid[1]?.toInt()
        )
    }

    fun getAllSpecialities(href: String): List<SpecialityDataItem> {
        val fullHref = "$BASE_URL_UNIVER$href/spec"

        val doc = Jsoup.connect(fullHref).get()

        return doc.select("div.itemSpecAll")
            .map { speciality -> getSpeciality(speciality) }
    }

    fun getSpeciality(specialityInfo: Element): SpecialityDataItem {

        val budget: List<String?>?
        val paid: List<String?>?

        // title and href
        val title = specialityInfo.select("a.spectittle").text()
        val url = specialityInfo.select("a.spectittle").attr("href")

        // study form
        val studyForm = specialityInfo.select("div.itemSpecAllinfo")
            .select("div > i").text()

        // exams
        val exams = specialityInfo.select("div.egeInVuzProg > span")
            .text()

        val specialityNums = specialityInfo.select("div.col-md-5").select("div.col-md-4.itemSpecAllParamWHide.newbl")

        // cost
        val cost = specialityNums[0].select("center").select("center > a.tooltipq").first()
            .ownText().filter { it.isDigit() }


        // budget
        budget = if (specialityNums[1].ownText() == "нет") null
        else specialityNums[1].select("center > a.tooltipq")
            .map { element -> element.ownText().filter { it.isDigit() } }
            .map { if (it.isEmpty() or it.isBlank()) null else it }

        // paid
        paid = if (specialityNums[2].ownText() == "нет") null
        else specialityNums[2].select("center > a.tooltipq")
            .map { element -> element.ownText().filter { it.isDigit() } }
            .map { if (it.isEmpty() or it.isBlank()) null else it }

        return SpecialityDataItem(
            url, title, studyForm, exams,
            if (cost == "") null else cost.toInt(),
            if (budget?.get(0) == "" || budget?.get(0) == null) null else budget[0]?.toInt(),
            if (budget?.get(1) == "" || budget?.get(1) == null) null else budget[1]?.toInt(),
            if (paid?.get(0) == "" || paid?.get(0) == null) null else paid[0]?.toInt(),
            if (paid?.get(1) == "" || paid?.get(1) == null) null else paid[1]?.toInt()
        )
    }

    fun getAllPrograms(url: String): List<ProgramDataItem> {
        val fullUrl = "$BASE_URL_UNIVER${url}programs/bakispec"
        return Jsoup.connect(fullUrl).get().select("div.itemSpecAll")
            .map { div -> getProgram(div) }
    }

    fun getProgram(programElement: Element): ProgramDataItem {
        val budget: List<String?>?
        val paid: List<String?>?

        // title and href
        val title = programElement.select("a.spectittle").text()
        val url = programElement.select("a.spectittle").attr("href")

        // exams
        val exams = programElement.select("div.egeInVuzProg > span")
            .text()

        // direction
        val napr = programElement.select("div.itemSpecAllinfo > div")[1].select("a").text()

        // program nums
        val programNums = programElement.select("div.col-md-5").select("div.col-md-4.itemSpecAllParamWHide.newbl")

        // cost
        val cost = programNums[0].select("center").select("center > a.tooltipq").first()
            .ownText().filter { it.isDigit() }


        // budget
        budget = if (programNums[1].ownText() == "нет") null
        else programNums[1].select("center > a.tooltipq")
            .map { element -> element.ownText().filter { it.isDigit() } }
            .map { if (it.isEmpty() or it.isBlank()) null else it }

        // paid
        paid = if (programNums[2].ownText() == "нет") null
        else programNums[2].select("center > a.tooltipq")
            .map { element -> element.ownText().filter { it.isDigit() } }
            .map { if (it.isEmpty() or it.isBlank()) null else it }

        return ProgramDataItem(
            url, title, napr, exams,
            if (cost == "") null else cost.toInt(),
            if (budget?.get(0) == "" || budget?.get(0) == null) null else budget[0]?.toInt(),
            if (budget?.get(1) == "" || budget?.get(1) == null) null else budget[1]?.toInt(),
            if (paid?.get(0) == "" || paid?.get(0) == null) null else paid[0]?.toInt(),
            if (paid?.get(1) == "" || paid?.get(1) == null) null else paid[1]?.toInt()
        )
    }


    fun getUniversitiesCards(): Elements {
        return allUniversitiesInNNPage.select("div.vuzesfullnorm")
    }

    private fun getPageCount(): Int =
        allUniversitiesInNNPage.select("div.pagpag")
            .select("ul.pagination")
            .select("ul > li")
            .size - 1
}