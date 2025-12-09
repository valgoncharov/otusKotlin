package examples

fun demonstrateOOPAndExtensions() {
    println("\n=== ООП, расширения и DSL ===\n")
    
    println("1. Классы и объекты")
    val user = User("John", 25)
    println(user.greet())
    
    val result = TestResult("loginTest", true)
    println("Test result: $result")
    
    println("\n2. Конструкторы и свойства")
    val testCase = TestCase("Login Test", "NEW", 1)
    println("Test: ${testCase.name}, Status: ${testCase.status}")
    
    testCase.status = "RUNNING"
    println("Updated status: ${testCase.status}")
    
    println("\n3. Наследование и интерфейсы")
    val loginTest = LoginTest()
    println("Running ${loginTest.name}: ${if (loginTest.execute()) "PASS" else "FAIL"}")
    
    println("\n4. Функции-расширения")
    val email = "test@example.com"
    println("Email '$email' is valid: ${email.isValidEmail()}")
    
    val invalidEmail = "test.example"
    println("Email '$invalidEmail' is valid: ${invalidEmail.isValidEmail()}")
    
    // Расширение для списков
    val tests = listOf("login", "logout", "purchase")
    println("Test summary: ${tests.toTestSummary()}")
    
    println("\n5. DSL пример")
    val testConfig = testConfiguration {
        name = "API Test Suite"
        timeout = 30
        retries = 3
        tags("smoke", "api")
    }
    println("Configuration: $testConfig")
}

// Обычный класс
class User(val name: String, var age: Int) {
    fun greet() = "Hello, I'm $name"
}

// Data класс - для хранения данных
data class TestResult(val testName: String, val passed: Boolean)

// Класс с валидацией
class TestCase(
    val name: String,           // только чтение
    var status: String = "NEW", // можно изменять
    private val priority: Int   // приватное свойство
) {
    init {
        require(name.isNotEmpty()) { "Test name cannot be empty" }
    }
}

// Интерфейс
interface Testable {
    fun execute(): Boolean
}

// Базовый класс
open class BaseTest(val name: String)

// Наследование
class LoginTest : BaseTest("Login"), Testable {
    override fun execute(): Boolean {
        // логика теста
        return true
    }
}

// Функции-расширения
fun String.isValidEmail(): Boolean {
    return this.contains("@") && this.contains(".")
}

fun List<String>.toTestSummary(): String {
    return "Total tests: ${this.size}, Tests: ${this.joinToString(", ")}"
}

// Простой DSL
class TestConfiguration {
    var name: String = ""
    var timeout: Int = 60
    var retries: Int = 0
    private val tags = mutableListOf<String>()
    
    fun tags(vararg tagNames: String) {
        tags.addAll(tagNames)
    }
    
    override fun toString(): String {
        return "TestConfiguration(name='$name', timeout=$timeout, retries=$retries, tags=$tags)"
    }
}

fun testConfiguration(config: TestConfiguration.() -> Unit): TestConfiguration {
    return TestConfiguration().apply(config)
}