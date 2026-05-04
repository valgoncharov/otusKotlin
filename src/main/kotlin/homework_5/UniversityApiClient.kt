package homework_5

import okhttp3.OkHttpClient
import okhttp3.Request
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UniversityApiClient {
    private val client = OkHttpClient()
    private val gson = Gson()

    fun getUniversitiesByCountry(country: String): List<University> {
        // Кодируем название страны для URL
        val encodedCountry = country.replace(" ", "%20")
        val url = "http://universities.hipolabs.com/search?country=$encodedCountry"

        println("📡 Отправляем запрос: $url")

        // Создаём запрос
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        // Выполняем запрос
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw Exception("Ошибка запроса: ${response.code}")
            }

            val json = response.body?.string() ?: throw Exception("Пустой ответ от сервера")

            // Парсим JSON в список университетов
            val type = object : TypeToken<List<University>>() {}.type
            return gson.fromJson(json, type)
        }
    }
}