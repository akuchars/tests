package akuchars.task.application.model

class TaskDto(
		val id: Long,
		val content: String,
		val title: String,
		val priority: TaskPriorityDto,
		val assignee: String,
		val period: PeriodDto? = null,
		val mainGoal: String? = null,
		val tagsDto: Set<TagDto> = setOf(),
		val subtasksInfo: SubtaskInfoDto,
		val taskStatus : TaskStatusDto
)

class PeriodDto(
		var start: String,
		var end: String? = null
)