//ДЗ 3: Функции-расширения, делегаты, DSL
//
//Цель:
//Реализовать функции-расширения.
//
//
//Описание/Пошаговая инструкция выполнения домашнего задания:
//🔥 Создайте DSL для удобного формирования HTTP-запросов! 🔥
//
//📌 Что нужно реализовать?
//✅ Основные HTTP-методы: get, post, put, delete ⚡️
//✅ Гибкую настройку URL и заголовков запроса 🛠
//✅ Возможность добавления тела запроса (если требуется) 📩
//
//💡 Как это должно работать?
//
//fun main() {
//    val request = post {
//        url("https://example.com/api")
//        header {
//            contentType("application/json")
//            authorization("Bearer token")
//        }
//        body("{\"name\": \"John\", \"age\": 30}")
//    }
//}


// Основные классы для HTTP запроса
data class HttpRequest(
    val method: String,
    val url: String,
    val headers: MutableMap<String, String> = mutableMapOf(),
    var body: String? = null
) {
    override fun toString(): String {
        return """
$method $url
Headers: $headers
Body: $body
""".trimIndent()
    }
}

// Класс для построения заголовков
class HeadersBuilder {
    private val headers = mutableMapOf<String, String>()

    fun header(key: String, value: String) {
        headers[key] = value
    }

    // Методы для заголовков
    fun contentType(value: String) = header("Content-Type", value)
    fun authorization(value: String) = header("Authorization", value)
    fun userAgent(value: String) = header("User-Agent", value)
    fun accept(value: String) = header("Accept", value)

    fun build(): MutableMap<String, String> = headers
}

// Класс для построения запроса
class RequestBuilder(private val method: String) {
    private var url: String = ""
    private val headersBuilder = HeadersBuilder()
    private var body: String? = null

    fun url(url: String) {
        this.url = url
    }

    fun header(block: HeadersBuilder.() -> Unit) {
        headersBuilder.block()
    }

    fun body(body: String) {
        this.body = body
    }

    // Функция для JSON тела
    fun jsonBody(block: () -> String) {
        this.body = block()
        headersBuilder.contentType("application/json")
    }

    fun build(): HttpRequest {
        if (url.isBlank()) {
            throw IllegalStateException("URL must be specified")
        }

        return HttpRequest(
            method = method,
            url = url,
            headers = headersBuilder.build(),
            body = body
        )
    }
}

// DSL функции для HTTP методов
fun get(block: RequestBuilder.() -> Unit): HttpRequest {
    return RequestBuilder("GET").apply(block).build()
}

fun post(block: RequestBuilder.() -> Unit): HttpRequest {
    return RequestBuilder("POST").apply(block).build()
}

fun put(block: RequestBuilder.() -> Unit): HttpRequest {
    return RequestBuilder("PUT").apply(block).build()
}

fun delete(block: RequestBuilder.() -> Unit): HttpRequest {
    return RequestBuilder("DELETE").apply(block).build()
}

fun json(block: JsonBuilder.() -> Unit): String {
    return JsonBuilder().apply(block).build()
}

class JsonBuilder {
    private val properties = mutableMapOf<String, Any>()

    fun property(key: String, value: Any) {
        properties[key] = value
    }

    fun build(): String {
        val jsonProperties = properties.map { (key, value) ->
            when (value) {
                is String -> "\"$key\": \"$value\""
                else -> "\"$key\": $value"
            }
        }.joinToString(", ")

        return "{$jsonProperties}"
    }
}

fun main() {
    println("=== POST запрос ===")
    val request = post {
        url("https://example.com/api")
        header {
            contentType("application/json")
            authorization("Bearer token123")
            userAgent("MyApp/1.0")
        }
        body("{\"name\": \"John\", \"age\": 30}")
    }
    println(request)

    println("\n=== GET запрос ===")
    val getRequest = get() {
        url("https://api.github.com/users")
        header {
            accept("application/json")
            authorization("token github_token")
        }
    }
    println(getRequest)

    println("\n=== PUT запрос с JSON билдером ===")
    val putRequest = put {
        url("https://example.com/api/users/1")
        header {
            contentType("application/json")
            authorization("Bearer token456")
        }
        jsonBody {
            json {
                property("name", "Alice")
                property("age", 25)
                property("active", true)
            }
        }
    }
    println(putRequest)

    println("\n=== DELETE запрос ===")
    val deleteRequest = delete {
        url("https://example.com/api/users/1")
        header {
            authorization("Bearer token789")
        }
    }
    println(deleteRequest)
}