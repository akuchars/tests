package akuchars.application.task.model

import akuchars.application.user.model.UserDto

class ProjectDto(
		val id: Long,
		val name: String,
		val owner: UserDto,
		val tasks: Set<TaskDto> = setOf()
)