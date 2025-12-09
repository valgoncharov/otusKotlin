package examples

fun demonstrateBasicSyntax() {
    println("\n=== Основы языка Kotlin ===\n")

    // val vs var
    println("1. Переменные: val vs var")
    val name = "QA Engineer"     // неизменяемая
    var score = 0                // изменяемая

    score = 100                  // ✅ Можно изменить
    // name = "Senior QA"        // ❌ Ошибка компиляции

    println("Name: $name")
    println("Score: $score")

    println("\n2. Типы данных")
    val age: Int = 25
    val height: Double = 175.5
    val isActive: Boolean = true
    val userName: String = "testuser"
    val response: String? = null    // nullable тип

    println("Age: $age, Height: $height, Active: $isActive")
    println("User: $userName, Response: $response")

    println("\n3. Null Safety")
    val surname: String? = null
    println("Surname length: ${surname?.length}")           // null
    println("Surname uppercase: ${surname?.uppercase()}")   // null

    val testName: String? = "LoginTest"
    println("Test name length: ${testName?.length}")        // 9

    println("\n4. Функции")
    println(greetUser("Test User"))
    println("Score calculation: ${calculateScore(80, 20)}")
    logTestResult(true)

    println("\n5. Условия")
    val testScore = 85
    val status = if (testScore >= 80) "Pass" else "Fail"
    println("Test status: $status")

    val grade = when (testScore) {
        in 90..100 -> "A"
        in 80..89 -> "B"
        in 70..79 -> "C"
        else -> "F"
    }
    println("Grade: $grade")

    println("\n6. Циклы")
    val testCases = listOf("login", "logout", "purchase")
    for (testCase in testCases) {
        println("Running test: $testCase")
    }

    for (i in 1..3) {
        println("Iteration $i")
    }

    println("\n7. Строки и интерполяция")
    val user = "testuser"
    val testId = 12345
    val message = "User $user failed test #$testId"
    println(message)

    val report = "Result: ${if (testScore > 80) "PASS" else "FAIL"}"
    println(report)
}

// Простая функция
fun greetUser(name: String): String {
    return "Hello, $name!"
}

// Краткая запись
fun calculateScore(base: Int, bonus: Int) = base + bonus

// Без возвращаемого значения
fun logTestResult(result: Boolean) {
    println("Test result: $result")
}