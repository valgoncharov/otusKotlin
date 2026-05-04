package homework_5

import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import org.bson.Document

class UniversityDatabase {
    private val connectionString = "mongodb://localhost:27017"
    private val databaseName = "university_catalog"
    private val collectionName = "universities"

    private val client = MongoClients.create(connectionString)
    private val database: MongoDatabase = client.getDatabase(databaseName)
    private val collection: MongoCollection<Document> = database.getCollection(collectionName)

    fun saveUniversities(universities: List<University>) {
        println("💾 Сохраняем ${universities.size} университетов в базу данных...")

        // Очищаем старые данные
        collection.drop()

        // Сохраняем каждый университет
        for (university in universities) {
            val doc = Document()
            doc.append("name", university.name)
            doc.append("country", university.country)
            doc.append("alpha_two_code", university.alphaTwoCode)
            doc.append("state_province", university.stateProvince)
            doc.append("domains", university.domains)
            doc.append("web_pages", university.webPages)

            collection.insertOne(doc)
        }

        println("✅ Данные успешно сохранены!")
    }

    fun searchUniversitiesByName(searchQuery: String): List<Document> {
        println("🔍 Ищем университеты по запросу: '$searchQuery'")

        // Создаём фильтр для поиска по названию (регистронезависимый)
        val filter = Filters.regex("name", searchQuery, "i")

        val results = mutableListOf<Document>()
        collection.find(filter).forEach { results.add(it) }

        return results
    }

    fun getAllUniversities(): List<Document> {
        val results = mutableListOf<Document>()
        collection.find().forEach { results.add(it) }
        return results
    }

    fun close() {
        client.close()
        println("🔌 Соединение с базой данных закрыто")
    }
}