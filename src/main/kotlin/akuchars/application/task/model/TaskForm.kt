package akuchars.application.task.model

class TaskForm(
		var content: String,
		var title: String,
		var priority: TaskPriorityDto,
		var projectId: Long,
		var period: PeriodForm? = null,
		var mainGoal: String? = null
)

class PeriodForm(
		var start: String,
		var end: String? = null
)