package akuchars.application.task.model

import akuchars.domain.task.model.TaskPriority

class TaskDto(
		val content: String,
		val title: String,
		val priority: TaskPriorityDto,
		val projectId: Long
)