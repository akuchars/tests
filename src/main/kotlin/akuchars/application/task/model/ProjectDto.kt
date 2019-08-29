package akuchars.application.task.model

class ProjectDto(
		val id: Long,
		val name: String,
		val tasks: Set<TaskDto> = setOf()
)