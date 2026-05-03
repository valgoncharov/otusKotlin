package HW_5

import java.net.HttpURLConnection
import java.net.URL
import java.util.Scanner

data class UniversityFind(
    val name: String,
    val country: String,
    val webPages: String
)

fun main() {
    println("КАТАЛОГ ВУЗОВ")

    val scanner = Scanner(System.`in`)

    println("\nВведите страну:")
    print("> ")
    val country = scanner.nextLine()

    val universities = loadUniversities(country)

    if (universities.isEmpty()) {
        println("Университеты не найдены")
        return
    }

    println("Загружено ${universities.size} университетов")

    var continueSearch = true
    while (continueSearch) {
        println("\nВведите название ВУЗа для поиска (или 'выход'):")
        print("> ")
        val search = scanner.nextLine()

        if (search.equals("выход", ignoreCase = true)) {
            continueSearch = false
            println("До свидания!")
            break
        }

        val results = universities.filter {
            it.name.contains(search, ignoreCase = true)
        }

        if (results.isEmpty()) {
            println("Ничего не найдено")
        } else {
            println("\nНайдено ${results.size} университетов:")
            results.forEachIndexed { index, uni ->
                println("${index + 1}. ${uni.name}")
                println("   🔗 ${uni.webPages}")
            }
        }
    }
}

fun loadUniversities(country: String): List<UniversityFind> {
    val encodedCountry = country.replace(" ", "%20")
    val url = URL("http://universities.hipolabs.com/search?country=$encodedCountry")

    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "GET"

    val responseCode = connection.responseCode
    if (responseCode != 200) {
        println("Ошибка: $responseCode")
        return emptyList()
    }

    val response = connection.inputStream.bufferedReader().use { it.readText() }

    return parseSimpleUniversities(response)
}

fun parseSimpleUniversities(json: String): List<UniversityFind> {
    val universities = mutableListOf<UniversityFind>()

    val objects = json.split("},")

    for (obj in objects) {
        val name = extractValue(obj, "name")
        val country = extractValue(obj, "country")
        val webPages = extractValue(obj, "web_pages")

        if (name.isNotEmpty() && country.isNotEmpty()) {
            universities.add(UniversityFind(name, country, webPages))
        }
    }

    return universities
}

fun extractValue(json: String, key: String): String {
    val pattern = "\"$key\":\"(.*?)\"".toRegex()
    return pattern.find(json)?.groupValues?.get(1) ?: ""
}