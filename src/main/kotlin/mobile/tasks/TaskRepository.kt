package mobile.tasks

class TaskRepository(
    private val storage: TaskStorage
) {
    fun listTasks(): List<TaskItem> = storage.getAll()
    fun createTask(task: TaskItem): TaskItem = storage.insert(task)
    fun removeTask(taskId: Long): Boolean = storage.delete(taskId)
}
