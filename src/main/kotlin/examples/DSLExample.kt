package examples

fun demonstrateDSL() {
    println("\n=== DSL для HTTP-запросов ===\n")
    
    println("1. GET запрос")
    val getRequest = get {
        url("https://api.example.com/users/123")
        header {
            authorization("Bearer token123")
            contentType("application/json")
        }
    }
    println(getRequest)
    
    println("\n2. POST запрос")
    val postRequest = post {
        url("https://api.example.com/auth/login")
        header {
            contentType("application/json")
        }
        body("""{"username": "testuser", "password": "testpass"}""")
    }
    println(postRequest)
    
    println("\n3. PUT запрос")
    val putRequest = put {
        url("https://api.example.com/users/123")
        header {
            authorization("Bearer token123")
            contentType("application/json")
        }
        body("""{"name": "Updated Name", "age": 30}""")
    }
    println(putRequest)
    
    println("\n4. DELETE запрос")
    val deleteRequest = delete {
        url("https://api.example.com/users/123")
        header {
            authorization("Bearer token123")
        }
    }
    println(deleteRequest)
    
    println("\n5. Практическое применение в тестах")
    demonstrateTestScenarios()
}

// Классы для DSL
data class HttpRequest(
    val method: String,
    val url: String,
    val headers: Map<String, String> = emptyMap(),
    val body: String? = null
) {
    override fun toString(): String {
        val headerStr = headers.entries.joinToString("\n  ") { "${it.key}: ${it.value}" }
        return """
            |HttpRequest {
            |  method: $method
            |  url: $url
            |  headers: {
            |    $headerStr
            |  }
            |  body: ${body ?: "null"}
            |}
        """.trimMargin()
    }
}

class HeaderBuilder {
    private val headers = mutableMapOf<String, String>()
    
    fun contentType(value: String) {
        headers["Content-Type"] = value
    }
    
    fun authorization(value: String) {
        headers["Authorization"] = value
    }
    
    fun custom(key: String, value: String) {
        headers[key] = value
    }
    
    internal fun build(): Map<String, String> = headers.toMap()
}

class HttpRequestBuilder(private val method: String) {
    private var url: String = ""
    private val headers = mutableMapOf<String, String>()
    private var body: String? = null
    
    fun url(value: String) {
        this.url = value
    }
    
    fun body(value: String) {
        this.body = value
    }
    
    fun header(config: HeaderBuilder.() -> Unit) {
        val headerBuilder = HeaderBuilder()
        headerBuilder.config()
        headers.putAll(headerBuilder.build())
    }
    
    internal fun build() = HttpRequest(method, url, headers, body)
}

// DSL функции
fun post(config: HttpRequestBuilder.() -> Unit): HttpRequest {
    return HttpRequestBuilder("POST").apply(config).build()
}

fun get(config: HttpRequestBuilder.() -> Unit): HttpRequest {
    return HttpRequestBuilder("GET").apply(config).build()
}

fun put(config: HttpRequestBuilder.() -> Unit): HttpRequest {
    return HttpRequestBuilder("PUT").apply(config).build()
}

fun delete(config: HttpRequestBuilder.() -> Unit): HttpRequest {
    return HttpRequestBuilder("DELETE").apply(config).build()
}

// Практический пример использования в тестах
fun demonstrateTestScenarios() {
    val baseUrl = "https://api.test.com"
    val token = "test-token-12345"
    
    println("=== Примеры DSL для тестирования ===")
    
    // Пример 1: DSL для описания тест-сьютов
    println("\n1. DSL для структурирования тестов:")
    val testSuite = test {
        group("API Tests") {
            setup { 
                println("   [SETUP] Подготовка тестового окружения")
                println("   [SETUP] Очистка БД, создание тестовых данных")
            }
            steps {
                step { 
                    println("   [STEP 1] Проверка доступности API")
                    // В реальном тесте здесь бы был вызов API
                    null // вернет null если ошибок нет
                }
                step {
                    println("   [STEP 2] Авторизация пользователя")
                    // Возвращаем код ошибки для демонстрации
                    "AUTH_FAILED" 
                }
                step {
                    println("   [STEP 3] Получение данных")
                    null
                }
            }
            teardown {
                println("   [TEARDOWN] Очистка после тестов")
            }
        }
    }
    testSuite.run()
    
    // Пример 2: Property-based тестирование
    println("\n\n2. Property-based тестирование с генерацией данных:")
    demonstratePropertyBasedTesting()
    
    println("\n\n3. Тестовый сценарий: E-commerce API")
    
    // Исходный пример с регистрацией
    val registerRequest = post {
        url("$baseUrl/auth/register")
        header {
            contentType("application/json")
        }
        body("""
            {
                "email": "newuser@test.com",
                "password": "SecurePass123",
                "name": "Test User"
            }
        """.trimIndent())
    }
    println("1. Регистрация:")
    println(registerRequest)
    
    // 2. Получение информации о пользователе
    val getUserRequest = get {
        url("$baseUrl/users/profile")
        header {
            authorization("Bearer $token")
            contentType("application/json")
        }
    }
    println("\n2. Получение профиля:")
    println(getUserRequest)
    
    // 3. Обновление профиля
    val updateProfileRequest = put {
        url("$baseUrl/users/profile")
        header {
            authorization("Bearer $token")
            contentType("application/json")
        }
        body("""{"bio": "QA Engineer specializing in API testing"}""")
    }
    println("\n3. Обновление профиля:")
    println(updateProfileRequest)
}

// DSL для структурированных тестов
class TestSuite {
    private val groups = mutableListOf<TestGroup>()
    
    fun group(name: String, config: TestGroup.() -> Unit) {
        val group = TestGroup(name)
        group.config()
        groups.add(group)
    }
    
    fun run() {
        groups.forEach { group ->
            println("\n  Группа тестов: ${group.name}")
            group.setupAction?.invoke()
            
            group.testSteps.forEach { (index, step) ->
                val error = step()
                if (error != null) {
                    println("   ❌ Шаг $index завершился с ошибкой: $error")
                } else {
                    println("   ✅ Шаг $index выполнен успешно")
                }
            }
            
            group.teardownAction?.invoke()
        }
    }
}

class TestGroup(val name: String) {
    var setupAction: (() -> Unit)? = null
    var teardownAction: (() -> Unit)? = null
    val testSteps = mutableListOf<Pair<Int, () -> String?>>()
    private var stepCounter = 0
    
    fun setup(action: () -> Unit) {
        setupAction = action
    }
    
    fun teardown(action: () -> Unit) {
        teardownAction = action
    }
    
    fun steps(config: StepsBuilder.() -> Unit) {
        val builder = StepsBuilder { step ->
            stepCounter++
            testSteps.add(stepCounter to step)
        }
        builder.config()
    }
}

class StepsBuilder(private val addStep: (() -> String?) -> Unit) {
    fun step(action: () -> String?) {
        addStep(action)
    }
}

fun test(config: TestSuite.() -> Unit): TestSuite {
    return TestSuite().apply(config)
}

// Property-based тестирование
fun demonstratePropertyBasedTesting() {
    // Простой генератор случайных чисел для демонстрации
    class SimpleArb(private val min: Double, private val max: Double) {
        fun generate(count: Int): List<Double> {
            return List(count) { 
                min + Math.random() * (max - min)
            }
        }
    }
    
    // Имитация проверки с генерированными данными
    println("  Запуск property-based теста с автоматической генерацией данных:")
    
    val arb = SimpleArb(-1900.0, 1000000.0)
    val testData = arb.generate(5) // генерируем 5 случайных значений
    
    println("  Сгенерированные тестовые данные: ")
    testData.forEachIndexed { index, value ->
        println("    Тест ${index + 1}: сумма = %.2f".format(value))
    }
    
    // Имитация теста биллинга
    class Bills {
        private var balance = 0.0
        
        fun payment(payer: String, persons: List<String>, sum: Double) {
            balance += sum
            // Логика распределения платежа
        }
        
        fun sum(): Double = balance
    }
    
    println("\n  Проверка инварианта: баланс должен быть близок к нулю")
    var passedTests = 0
    
    testData.forEach { sum ->
        val bills = Bills()
        val persons = listOf("Alice", "Bob", "Charlie")
        bills.payment(persons.first(), persons, sum)
        bills.payment(persons[1], persons, -sum/2)
        bills.payment(persons[2], persons, -sum/2)
        
        val finalBalance = bills.sum()
        val isValid = Math.abs(finalBalance) < 0.00001
        
        if (isValid) {
            passedTests++
            println("    ✅ Тест пройден: финальный баланс = %.5f".format(finalBalance))
        } else {
            println("    ❌ Тест провален: финальный баланс = %.5f".format(finalBalance))
        }
    }
    
    println("\n  Результат: $passedTests из ${testData.size} тестов пройдено")
    println("  💡 В реальных тестах можно генерировать тысячи комбинаций!")
}