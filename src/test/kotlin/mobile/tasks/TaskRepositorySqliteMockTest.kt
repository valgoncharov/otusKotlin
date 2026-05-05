package mobile.tasks

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TaskRepositorySqliteMockTest {

    @Test
    fun testCrudWithSqLiteMockStorage () {
        val storage = MockSqliteTaskStorage(
            initialTasks = listOf(
                TaskItem(id = 1, title = "first", done = false)
            )
        )
        val repository = TaskRepository(storage)

        val before = repository.listTasks()
        assertEquals(1, before.size)

        val created = repository.createTask(TaskItem(id = 2, title = "second", done = true))
        assertEquals(2, created.id)
        assertTrue(created.done)

        val afterCreate = repository.listTasks()
        assertEquals(2, afterCreate.size)

        val deleted = repository.removeTask(1)
        assertTrue(deleted)
        assertEquals(1, repository.listTasks().size)

        val notFoundDelete = repository.removeTask(999)
        assertFalse(notFoundDelete)
    }

    @Test
    fun testRepositoryDelegatesToSqLiteMockObject () {
        val storage = mockk<TaskStorage>()
        val repository = TaskRepository(storage)
        val task = TaskItem(id = 7, title = "mocked")

        every { storage.getAll() } returns listOf(task)
        every { storage.insert(task) } returns task
        every { storage.delete(7) } returns true

        assertEquals(listOf(task), repository.listTasks())
        assertEquals(task, repository.createTask(task))
        assertTrue(repository.removeTask(7))

        verify(exactly = 1) { storage.getAll() }
        verify(exactly = 1) { storage.insert(task) }
        verify(exactly = 1) { storage.delete(7) }
    }
}
