import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import org.litote.kmongo.*
import java.io.File
import kotlinx.serialization.Serializable

@Serializable
data class GostFormat(
    val bibliozapis:String? = null
)

val literature = mongoDatabase.getCollection<GostFormat>().apply { drop() }

fun main() {
    literature.insertMany(
        listOf(
            GostFormat("Исаев, И.А. Технология интересов: «рынки власти» как сети / И.А. Исаев. – Текст : непосредственный // История государства и права. – 2020. – № 1. – С. 3-10. - DOI: 10.18572/1812-3805-2020-1-3-10"),
            GostFormat("Россинская, Е. Р. Избранное / Е.Р. Россинская. - Москва : НОРМА, 2019. - 679 с. : 23 л. вклей., портр. - Библиогр. в подстр. примеч. - ISBN 978-5-00156-041-8."),
            GostFormat("Российская Федерация. Законы. Земельный кодекс Российской Федерации : текст с изм. и доп. вступ. в силу с 01.01.2019 : [принят Государственной Думой 28 сентября 2001 года : одобрен Советом Федерации 10 октября 2001 года]. – Москва, 2019. – 540 с."),
            GostFormat("Департамента науки, промышленной политики и предпринимательства г. Москвы : гос. учреждение. – 2019. – URL: http://www.dmpmos.ru (дата обращения: 06.06.2019). – Режим доступа: свободный.")
        )
    )

    print("Введите ключи библиографических записей: ")
    val keyword = readLine()
    val search = Regex(".*$keyword.*")
    val literatureList = literature.find(GostFormat::bibliozapis eq search).toList()
    val file = File("C:/Users/fedor/Downloads/kursovaya_Fedorov_20M/src/main/kotlin/main.html")

    file.writeText(
        StringBuilder().appendHTML().html {
            body {
                var count = 1
                literatureList.forEach {
                    h3 {
                        style = "text-align: left"
                        +"${count}) ${it.bibliozapis}"
                        count++
                    }
                }
            }
        }.toString()
    )
    file.exists()
}
