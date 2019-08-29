package akuchars.application.task.model

class TaskForm(
		var content: String,
		var title: String,
		var priority: TaskPriorityDto,
		var projectId: Long
)