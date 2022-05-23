import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import org.litote.kmongo.*
import java.io.File
import kotlinx.serialization.Serializable

@Serializable
data class GostBibtexFormat(
    val publisher:String? = null,
    val title:String? = null,
    val type:String? = null,
    val author:String? = null,
    val journal:String? = null,
    val address:String? = null,
    val year:String? = null,
    val volume:String? = null,
    val number:String? = null,
    val pages:String? = null,
    val note:String? = null,
    val isbn:String? = null,
    val url:String? = null,
    val keyword:String? = null
)

val literature = mongoDatabase.getCollection<GostBibtexFormat>().apply { drop() }

val journal = "publisher title / author – journal – year – number – pages – note"
val book = "publisher title / author – address year – volume – note – isbn"
val zakmat = "publisher title : journal – address year – volume"
val site = "publisher – year – url – note"

fun shablonBuilder(typeshablon: String, it2: GostBibtexFormat, count: Int): String{
    val arrayShablon = typeshablon.split(" ")
    val mapShablon = mapOf("publisher" to it2.publisher, "title" to it2.title, "author" to it2.author,  "journal" to it2.journal, "address" to it2.address, "year" to it2.year, "volume" to it2.volume, "number" to it2.number, "pages" to it2.pages, "note" to it2.note, "isbn" to it2.isbn, "url" to it2.url)
    var shablon = ""
    arrayShablon.forEach {
        if(it == "–" || it == "/" || it == ":"){
            shablon = shablon + " " + it
        }
        else if(mapShablon.containsKey(it)){
            shablon = shablon + " " + mapShablon.get(it)
        }
    }
    return count.toString() + shablon
}

fun main() {
    literature.insertOne(
        GostBibtexFormat(
            "Исаев, И.А.",
            "Технология интересов: «рынки власти» как сети",
            "статья из журнала",
            "И.А. Исаев.",
            "Текст : непосредственный // История государства и права.",
            "",
            "2020.",
            "",
            "№ 1.",
            "С. 3-10.",
            "DOI: 10.18572/1812-3805-2020-1-3-10",
            "",
            "",
            "Исаев, И.А. Технология интересов: «рынки власти» как сети / И.А. Исаев. – Текст : непосредственный // История государства и права. – 2020. – № 1. – С. 3-10. - DOI: 10.18572/1812-3805-2020-1-3-10"
        )
    )
    literature.insertOne(
        GostBibtexFormat(
            "Россинская, Е. Р.",
            "Избранное",
            "книга",
            "Е.Р. Россинская.",
            "",
            "Москва : НОРМА,",
            "2019.",
            "679 с. : 23 л. вклей., портр.",
            "",
            "",
            "Библиогр. в подстр. примеч.",
            "ISBN 978-5-00156-041-8.",
            "",
            "Россинская, Е. Р. Избранное / Е.Р. Россинская. - Москва : НОРМА, 2019. - 679 с. : 23 л. вклей., портр. - Библиогр. в подстр. примеч. - ISBN 978-5-00156-041-8."
        )
    )
    literature.insertOne(
        GostBibtexFormat(
            "Российская Федерация. Законы.",
            "Земельный кодекс Российской Федерации : текст с изм. и доп. вступ. в силу с 01.01.2019",
            "законодательный материал",
            "",
            "[принят Государственной Думой 28 сентября 2001 года : одобрен Советом Федерации 10 октября 2001 года].",
            "Москва,",
            "2019.",
            "540 с.",
            "",
            "",
            "",
            "",
            "",
            "Российская Федерация. Законы. Земельный кодекс Российской Федерации : текст с изм. и доп. вступ. в силу с 01.01.2019 : [принят Государственной Думой 28 сентября 2001 года : одобрен Советом Федерации 10 октября 2001 года]. – Москва, 2019. – 540 с."
        )
    )
    literature.insertOne(
        GostBibtexFormat(
            "Департамента науки, промышленной политики и предпринимательства г. Москвы : гос. учреждение.",
            "",
            "сайт",
            "",
            "",
            "",
            "2019.",
            "",
            "",
            "",
            "Режим доступа: свободный.",
            "",
            "URL: http://www.dmpmos.ru (дата обращения: 06.06.2019).",
            "Департамента науки, промышленной политики и предпринимательства г. Москвы : гос. учреждение. – 2019. – URL: http://www.dmpmos.ru (дата обращения: 06.06.2019). – Режим доступа: свободный."
        )
    )

    print("Введите ключи библиографических записей: ")
    val keyword = readLine()
    val search = Regex(".*$keyword.*")
    val literatureList = literature.find(GostBibtexFormat::keyword eq search).toList()
    val file = File("C:/Users/fedor/Downloads/kursovaya_Fedorov_20M/src/main/kotlin/main.html")

    file.writeText(
        StringBuilder().appendHTML().html {
            body {
                var count = 1
                literatureList.forEach {
                    when(it.type) {
                        "статья из журнала" -> h3 {
                            style = "text-align: left"
                            +shablonBuilder(journal, it, count)
                        }
                        "книга" -> h3 {
                            style = "text-align: left"
                            +shablonBuilder(book, it, count)
                        }
                        "законодательный материал" -> h3 {
                            style = "text-align: left"
                            +shablonBuilder(zakmat, it, count)
                        }
                        "сайт" -> h3 {
                            style = "text-align: left"
                            +shablonBuilder(site, it, count)
                        }
                    }
                    count++
                }
            }
        }.toString()
    )
    file.exists()
}
