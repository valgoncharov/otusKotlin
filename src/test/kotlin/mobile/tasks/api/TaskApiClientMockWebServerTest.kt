package mobile.tasks.api

import mobile.tasks.TaskItem
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TaskApiClientMockWebServerTest {

    private lateinit var server: MockWebServer
    private lateinit var apiClient: TaskApiClient

    @BeforeEach
    fun setUp() {
        server = MockWebServer()
        server.start()
        apiClient = TaskApiClient(server.url("/").toString().removeSuffix("/"))
    }

    @AfterEach
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun testGetReturnsTaskList () {
        server.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody("""[{"id":1,"title":"from server","done":false}]""")
                .addHeader("Content-Type", "application/json")
        )

        val result = apiClient.getTasks()
        val recorded = server.takeRequest()

        assertEquals("GET", recorded.method)
        assertEquals("/tasks", recorded.path)
        assertEquals(1, result.size)
        assertEquals("from server", result.first().title)
    }

    @Test
    fun testPostCreatesTask () {
        server.enqueue(
            MockResponse()
                .setResponseCode(201)
                .setBody("""{"id":2,"title":"created","done":true}""")
                .addHeader("Content-Type", "application/json")
        )

        val created = apiClient.createTask(TaskItem(id = 2, title = "created", done = true))
        val recorded = server.takeRequest()

        assertEquals("POST", recorded.method)
        assertEquals("/tasks", recorded.path)
        assertTrue(recorded.body.readUtf8().contains("\"id\":2"))
        assertEquals(2, created.id)
        assertTrue(created.done)
    }

    @Test
    fun testDeleteRemovesTask () {
        server.enqueue(MockResponse().setResponseCode(204))

        val result = apiClient.deleteTask(9)
        val recorded = server.takeRequest()

        assertEquals("DELETE", recorded.method)
        assertEquals("/tasks/9", recorded.path)
        assertTrue(result)
    }
}
