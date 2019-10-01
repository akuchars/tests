package akuchars.application.task.model

class TaskForm(
		var id: Long? = null,
		var content: String,
		var title: String,
		var priority: TaskPriorityDto,
		var projectId: Long,
		var period: PeriodForm? = null,
		var mainGoal: String? = null,
		var tags: Set<TagForm> = setOf(),
		var subtasks: Set<SubtaskForm> = setOf()
)

class PeriodForm(
		var start: String,
		var end: String? = null
)