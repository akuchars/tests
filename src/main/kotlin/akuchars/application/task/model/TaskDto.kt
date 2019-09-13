package akuchars.application.task.model

class TaskDto(
		val id: Long,
		val content: String,
		val title: String,
		val priority: TaskPriorityDto,
		val assignee: String,
		val period: PeriodDto? = null,
		val mainGoal: String? = null
)

class PeriodDto(
		var start: String,
		var end: String? = null
)