package akuchars.application.task.model

import akuchars.application.common.model.ErrorDto
import akuchars.application.user.model.FrontUserDto

class DetailProjectDto(
		id: Long,
		name: String,
		tasks: Set<TaskDto> = setOf(),
		val users: Set<FrontUserDto>
) : ProjectDto(id, name, tasks)

open class ProjectDto(
		val id: Long,
		val name: String,
		val tasks: Set<TaskDto> = setOf(),
		val error: ErrorDto? = null
)