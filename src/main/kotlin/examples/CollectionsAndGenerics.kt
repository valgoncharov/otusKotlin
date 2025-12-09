package examples

fun demonstrateCollectionsAndGenerics() {
    println("\n=== Коллекции и обобщенные типы ===\n")
    
    println("1. Основные типы коллекций")
    // List - упорядоченная коллекция
    val testNames = listOf("login", "logout", "purchase")
    val mutableTests = mutableListOf("signup")
    mutableTests.add("delete_user")
    
    println("Tests: $testNames")
    println("Mutable tests: $mutableTests")
    
    // Set - уникальные элементы
    val browsers = setOf("Chrome", "Firefox", "Safari", "Chrome")
    println("Unique browsers: $browsers")
    
    // Map - пары ключ-значение
    val testResults = mapOf(
        "login" to true,
        "logout" to false,
        "purchase" to true
    )
    println("Test results: $testResults")
    
    println("\n2. Immutable vs Mutable")
    val immutableUsers = listOf("user1", "user2")
    // immutableUsers.add("user3") // Ошибка компиляции
    
    val mutableCache = mutableMapOf<String, Any>()
    mutableCache["lastResponse"] = "OK"
    mutableCache["statusCode"] = 200
    println("Cache: $mutableCache")
    
    println("\n3. Generics")
    // Без generics
    val items: List<Any> = listOf("text", 123, true)
    println("Mixed items: $items")
    
    // С generics
    val names: List<String> = listOf("Alice", "Bob")
    val scores: List<Int> = listOf(95, 87, 92)
    println("Names: $names, Scores: $scores")
    
    // Обобщенная функция
    printList(listOf("test1", "test2"))
    printList(listOf(1, 2, 3))
    
    println("\n4. Операции с коллекциями")
    val tests = listOf("login", "logout", "purchase", "admin")
    
    // Фильтрация
    val shortTests = tests.filter { it.length <= 5 }
    println("Short tests: $shortTests")
    
    // Преобразование
    val upperTests = tests.map { it.uppercase() }
    println("Uppercase tests: $upperTests")
    
    // Поиск
    val loginTest = tests.find { it == "login" }
    println("Found login test: $loginTest")
    
    // Проверки
    val hasAdmin = tests.any { it == "admin" }
    val allShort = tests.all { it.length < 10 }
    println("Has admin: $hasAdmin, All short: $allShort")
    
    println("\n5. Группировка и агрегация")
    val testResultsList = listOf(
        "login" to true,
        "logout" to false,
        "purchase" to true,
        "admin" to false,
        "settings" to true
    )
    
    // Группировка по результату
    val grouped = testResultsList.groupBy { it.second }
    println("Grouped by result:")
    grouped.forEach { (passed, tests) ->
        println("  $passed: ${tests.map { it.first }}")
    }
    
    // Подсчет
    val passedCount = testResultsList.count { it.second }
    val totalCount = testResultsList.size
    println("Passed: $passedCount/$totalCount")
    
    println("\n6. Практическое применение")
    demonstrateTestResultAnalysis()
}

// Обобщенная функция
fun <T> printList(items: List<T>) {
    print("List of ${items.firstOrNull()?.let { it::class.simpleName } ?: "Unknown"}: ")
    for (item in items) {
        print("$item ")
    }
    println()
}

// Практический пример для тестов
data class DetailedTestResult(
    val name: String, 
    val status: String, 
    val duration: Int
)

fun demonstrateTestResultAnalysis() {
    val testResults = listOf(
        DetailedTestResult("login", "PASS", 150),
        DetailedTestResult("logout", "FAIL", 80),
        DetailedTestResult("purchase", "PASS", 300),
        DetailedTestResult("search", "PASS", 120),
        DetailedTestResult("filter", "FAIL", 200)
    )
    
    // Группировка по статусу
    val byStatus = testResults.groupBy { it.status }
    println("\nTest results by status:")
    byStatus.forEach { (status, results) ->
        println("  $status: ${results.map { it.name }}")
    }
    
    // Средняя длительность по группам
    val avgDuration = testResults.groupBy { it.status }
        .mapValues { entry -> 
            entry.value.map { it.duration }.average() 
        }
    println("\nAverage duration by status:")
    avgDuration.forEach { (status, avg) ->
        println("  $status: ${avg.toInt()}ms")
    }
    
    // Самый долгий тест
    val longestTest = testResults.maxByOrNull { it.duration }
    println("\nLongest test: ${longestTest?.name} (${longestTest?.duration}ms)")
}