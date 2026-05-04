package homework_5

import java.util.Scanner

fun main() {
    println("=" .repeat(50))
    println("🎓 КАТАЛОГ ВУЗОВ 🎓")
    println("=" .repeat(50))

    val scanner = Scanner(System.`in`)
    val apiClient = UniversityApiClient()
    val database = UniversityDatabase()

    try {
        // Шаг 1: Ввод страны
        println("\n🌍 Введите страну для поиска университетов:")
        print("> ")
        val country = scanner.nextLine()

        if (country.isBlank()) {
            println("❌ Страна не может быть пустой!")
            return
        }

        // Шаг 2: Загрузка данных из API
        println("\n⏳ Загружаем данные...")
        val universities = try {
            apiClient.getUniversitiesByCountry(country)
        } catch (e: Exception) {
            println("❌ Ошибка при загрузке данных: ${e.message}")
            return
        }

        if (universities.isEmpty()) {
            println("⚠️ Университеты для страны '$country' не найдены")
            return
        }

        println("✅ Найдено ${universities.size} университетов")

        // Шаг 3: Сохранение в MongoDB
        database.saveUniversities(universities)

        // Шаг 4: Поиск по названию
        var continueSearching = true

        while (continueSearching) {
            println("\n" + "-" .repeat(50))
            println("🔎 Что вы хотите сделать?")
            println("1️⃣ Поиск университетов по названию")
            println("2️⃣ Показать все университеты")
            println("3️⃣ Выйти из программы")
            print("Выберите опцию (1-3): ")

            when (scanner.nextLine()) {
                "1" -> searchUniversities(scanner, database)
                "2" -> showAllUniversities(database)
                "3" -> {
                    println("👋 До свидания!")
                    continueSearching = false
                }
                else -> println("❌ Неверная опция. Попробуйте снова.")
            }
        }

    } catch (e: Exception) {
        println("❌ Произошла ошибка: ${e.message}")
        e.printStackTrace()
    } finally {
        database.close()
    }
}

fun searchUniversities(scanner: Scanner, database: UniversityDatabase) {
    println("\n🔍 Введите название университета для поиска:")
    print("> ")
    val searchQuery = scanner.nextLine()

    if (searchQuery.isBlank()) {
        println("❌ Запрос для поиска не может быть пустым!")
        return
    }

    val results = database.searchUniversitiesByName(searchQuery)

    if (results.isEmpty()) {
        println("❌ Университеты по запросу '$searchQuery' не найдены")
    } else {
        println("\n📚 Найдено ${results.size} университетов:\n")
        for ((index, uni) in results.withIndex()) {
            println("${index + 1}. ${uni.getString("name")}")
            println("   🌍 Страна: ${uni.getString("country")}")

            val stateProvince = uni.getString("state_province")
            if (stateProvince != null && stateProvince.isNotEmpty()) {
                println("   📍 Регион: $stateProvince")
            }

            val domains = uni.getList("domains", String::class.java)
            if (domains.isNotEmpty()) {
                println("   🌐 Домен: ${domains.joinToString(", ")}")
            }

            val webPages = uni.getList("web_pages", String::class.java)
            if (webPages.isNotEmpty()) {
                println("   🔗 Сайт: ${webPages.joinToString(", ")}")
            }
            println()
        }
    }
}

fun showAllUniversities(database: UniversityDatabase) {
    val allUniversities = database.getAllUniversities()

    if (allUniversities.isEmpty()) {
        println("📭 База данных пуста. Сначала загрузите университеты.")
        return
    }

    println("\n📚 Всего университетов в базе: ${allUniversities.size}\n")

    for ((index, uni) in allUniversities.withIndex()) {
        println("${index + 1}. ${uni.getString("name")}")
        println("   🌍 Страна: ${uni.getString("country")}")

        val webPages = uni.getList("web_pages", String::class.java)
        if (webPages.isNotEmpty()) {
            println("   🔗 Сайт: ${webPages.first()}")
        }
        println()

        // Показываем только первые 20, чтобы не перегружать консоль
        if (index == 19 && allUniversities.size > 20) {
            println("... и ещё ${allUniversities.size - 20} университетов")
            println("💡 Используйте поиск для более точного нахождения нужного вуза")
            break
        }
    }
}