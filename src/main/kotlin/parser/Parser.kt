package parser

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class Parser {
    companion object {
        const val BASE_URL_ALL_UNIVERSITIES = "https://vuzopedia.ru/region/city/67"
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

    fun getAllUniversitiesCards() {
        val pages = getPageCount()

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