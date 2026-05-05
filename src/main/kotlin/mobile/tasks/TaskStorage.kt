package mobile.tasks

interface TaskStorage {
    fun getAll(): List<TaskItem>
    fun insert(task: TaskItem): TaskItem
    fun delete(taskId: Long): Boolean
}
