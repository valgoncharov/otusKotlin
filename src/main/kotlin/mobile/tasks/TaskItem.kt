package mobile.tasks

data class TaskItem(
    val id: Long,
    val title: String,
    val done: Boolean = false
)
