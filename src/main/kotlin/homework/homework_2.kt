package homework

/*ДЗ 2: Коллекции

Описание/Пошаговая инструкция выполнения домашнего задания:
1️⃣ Группировка людей по возрасту 👥
Дан список людей с их именами и возрастом.
Сгруппируйте людей по возрасту и выведите количество людей в каждой возрастной группе.
 */
data class Person(val name: String, val age: Int): Comparable<Person>{
    override fun compareTo(other: Person): Int = age - other.age
    override fun toString(): String = "$name ($age)"
}

fun main() {
    val people = listOf(
        Person("Петя", 25),
        Person("Вася", 30),
        Person("Даша", 25),
        Person("Женя", 30),
        Person("Алексей", 20),
    )
// 1
    // Группировка по возрасту
    val groupByAge = people.groupBy { it.age }
    println(groupByAge)
    //Количество людей в каждой группе
    groupByAge.forEach { (age, persons) ->
        println("Возраст $age: ${persons.size} человек(а)")
        // Сортировка по возростанию
        val sortedByAge = people.sorted()
        println(sortedByAge)
        // Сортировка по убыванию
        val sortedByAgeDes = people.sortedByDescending { it.age }
        println(sortedByAgeDes)


        /*
    2️⃣ Фильтрация и группировка слов 🔠
    Дан список слов. Самому придумать?
    Извлеките из списка только те слова,
    которые начинаются с буквы "A" и сгруппируйте их по количеству букв.

    🚀 Удачи в выполнении!
     */
        //Дан список свой
        val words = listOf(
            "Apple", "Ant", "Banana", "Air",
            "Cat", "Axe", "Alaska", "Dog"
        )
        //Фильтр по А
        val result = words
            .filter { it.startsWith("A") }
            .groupBy { it.length }

        println(result)
        //Колвичество А
        val countByLength = words
            .filter { it.startsWith("A") }
            .groupingBy { it.length }
            .eachCount()

        println(countByLength)
    }
}

