package mobile.tasks.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mobile.tasks.TaskItem
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class TaskApiClient(
    private val baseUrl: String,
    private val client: OkHttpClient = OkHttpClient(),
    private val gson: Gson = Gson()
) {
    fun getTasks(): List<TaskItem> {
        val request = Request.Builder()
            .url("$baseUrl/tasks")
            .get()
            .build()
        client.newCall(request).execute().use { response ->
            check(response.isSuccessful) { "GET /tasks failed: ${response.code}" }
            val body = response.body?.string().orEmpty()
            val type = object : TypeToken<List<TaskItem>>() {}.type
            return gson.fromJson(body, type)
        }
    }

    fun createTask(task: TaskItem): TaskItem {
        val payload = gson.toJson(task)
            .toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("$baseUrl/tasks")
            .post(payload)
            .build()
        client.newCall(request).execute().use { response ->
            check(response.isSuccessful) { "POST /tasks failed: ${response.code}" }
            val body = response.body?.string().orEmpty()
            return gson.fromJson(body, TaskItem::class.java)
        }
    }

    fun deleteTask(taskId: Long): Boolean {
        val request = Request.Builder()
            .url("$baseUrl/tasks/$taskId")
            .delete()
            .build()
        client.newCall(request).execute().use { response ->
            return response.isSuccessful
        }
    }
}
