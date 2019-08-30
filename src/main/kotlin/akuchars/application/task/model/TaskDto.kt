package akuchars.application.task.model

class TaskDto(
		val id: Long,
		val content: String,
		val title: String,
		val priority: TaskPriorityDto,
		val assignee: String
)