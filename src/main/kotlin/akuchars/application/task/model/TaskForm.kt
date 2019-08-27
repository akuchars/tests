package akuchars.application.task.model

import akuchars.domain.task.model.TaskPriority

class TaskForm(
		var content: String,
		var title: String,
		var priority: TaskPriorityDto,
		var projectId: Long
)