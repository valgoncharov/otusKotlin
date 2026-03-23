package homeworkDSL

enum class HttpMethod {
    GET, POST, PUT, DELETE
}

data class Request(
    val method: HttpMethod,
    val url: String,
    val headers: MutableMap<String, String> = mutableMapOf(),
    val body: String? = null
)

data class HeadersBuilder {
    private val headers = mutableMapOf<String, String>()

    fun contentType(value: String) {
        headers["Content-Type"] = value
    }

    fun authorization(value: String) {
        headers["Authorization"] = value
    }

    fun accept(value: String) {
        headers["Accept"] = value
    }

    fun custom(name: String, value: String) {
        headers[name] = value
    }

    fun build(): Map<String, String> = headers.toMap()
}

class RequestBuilder(private val method: HttpMethod) {
    private var url: String = ""
    private val headersBuilder = HeadersBuilder()
    private var body: String? = null

    fun url(url: String) {
        this.url = url
    }

    fun header(init: HeadersBuilder.() -> Unit) {
        headersBuilder.init()
    }

    fun body(body: String) {
        this.body = body
    }

    fun build(): Request {
        return Request(
            method = method,
            url = url,
            headers = headersBuilder.build().toMutableMap(),
            body = body
        )
    }
}
fun get(init: RequestBuilder.() -> Unit): Request {
    return RequestBuilder(HttpMethod.GET).apply(init).build()
}

fun post(init: RequestBuilder.() -> Unit): Request {
    return RequestBuilder(HttpMethod.POST).apply(init).build()
}

fun put(init: RequestBuilder.() -> Unit): Request {
    return RequestBuilder(HttpMethod.PUT).apply(init).build()
}

fun delete(init: RequestBuilder.() -> Unit): Request {
    return RequestBuilder(HttpMethod.DELETE).apply(init).build()
}
// Симуляция отправки HTTP запроса
fun Request.send() {
    println("🚀 Отправка ${method.name} запроса на $url")
    println("📋 Заголовки:")
    headers.forEach { (key, value) ->
        println("   $key: $value")
    }
    if (body != null) {
        println("📦 Тело запроса: $body")
    }
    println("✅ Запрос отправлен!\n")
}

// Функция для имитации ответа
fun Request.sendAndReceive(): Response {
    println("📡 Отправка запроса...")
    return Response(
        statusCode = 200,
        body = "{\"status\": \"success\"}",
        headers = mapOf("Content-Type" to "application/json")
    )
}

data class Response(
    val statusCode: Int,
    val body: String,
    val headers: Map<String, String>
)
fun main() {
    // Пример 1: GET запрос
    val getRequest = get {
        url("https://api.example.com/users")
        header {
            accept("application/json")
            authorization("Bearer abc123")
        }
    }
    getRequest.send()

    // Пример 2: POST запрос с телом (как в задании)
    val postRequest = post {
        url("https://example.com/api")
        header {
            contentType("application/json")
            authorization("Bearer token")
        }
        body("{\"name\": \"John\", \"age\": 30}")
    }
    postRequest.send()

    // Пример 3: PUT запрос
    val putRequest = put {
        url("https://api.example.com/users/1")
        header {
            contentType("application/json")
            authorization("Bearer token")
        }
        body("{\"name\": \"John Updated\"}")
    }
    putRequest.send()

    // Пример 4: DELETE запрос
    val deleteRequest = delete {
        url("https://api.example.com/users/1")
        header {
            authorization("Bearer token")
        }
    }
    deleteRequest.send()

    // Пример 5: GET запрос с кастомным заголовком
    val customRequest = get {
        url("https://api.example.com/search")
        header {
            accept("application/json")
            custom("X-API-Version", "2.0")
            custom("X-Request-ID", "12345")
        }
    }
    customRequest.send()

    // Пример с получением ответа
    val response = postRequest.sendAndReceive()
    println("📥 Ответ: ${response.statusCode} - ${response.body}")
}
// Более продвинутая версия DSL
class AdvancedRequestBuilder(private val method: HttpMethod) {
    private var url: String = ""
    private val headers = mutableMapOf<String, String>()
    private var body: String? = null
    private var timeout: Int = 30000
    private var retries: Int = 0

    fun url(url: String) {
        this.url = url
    }

    fun headers(init: MutableMap<String, String>.() -> Unit) {
        headers.init()
    }

    fun header(name: String, value: String) {
        headers[name] = value
    }

    fun body(body: String) {
        this.body = body
    }

    fun json(body: Map<String, Any?>) {
        this.body = buildJson(body)
        headers["Content-Type"] = "application/json"
    }

    fun timeout(millis: Int) {
        this.timeout = millis
    }

    fun retries(count: Int) {
        this.retries = count
    }

    private fun buildJson(map: Map<String, Any?>): String {
        return map.entries.joinToString(
            prefix = "{",
            postfix = "}"
        ) { (key, value) ->
            "\"$key\": ${formatValue(value)}"
        }
    }

    private fun formatValue(value: Any?): String {
        return when (value) {
            is String -> "\"$value\""
            null -> "null"
            else -> value.toString()
        }
    }

    fun build(): Request {
        return Request(
            method = method,
            url = url,
            headers = headers.toMutableMap(),
            body = body
        )
    }
}

// Расширенные функции для продвинутого DSL
fun advancedGet(init: AdvancedRequestBuilder.() -> Unit): Request {
    return AdvancedRequestBuilder(HttpMethod.GET).apply(init).build()
}

fun advancedPost(init: AdvancedRequestBuilder.() -> Unit): Request {
    return AdvancedRequestBuilder(HttpMethod.POST).apply(init).build()
}

// Пример использования расширенной версии
fun advancedExample() {
    val request = advancedPost {
        url("https://api.example.com/users")
        headers {
            put("Authorization", "Bearer token123")
            put("Accept", "application/json")
        }
        json(mapOf(
            "name" to "Jane Doe",
            "age" to 28,
            "email" to "jane@example.com",
            "active" to true
        ))
        timeout(5000)
        retries(3)
    }
    request.send()
}