package mobile.tasks

class MockSqliteTaskStorage(
    initialTasks: List<TaskItem> = emptyList()
) : TaskStorage {

    private val data = LinkedHashMap<Long, TaskItem>()

    init {
        initialTasks.forEach { data[it.id] = it }
    }

    override fun getAll(): List<TaskItem> = data.values.toList()

    override fun insert(task: TaskItem): TaskItem {
        data[task.id] = task
        return task
    }

    override fun delete(taskId: Long): Boolean = data.remove(taskId) != null
}
