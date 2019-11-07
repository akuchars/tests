package akuchars.application.task.model

class SubtaskInfoDto(
		val subtasks: List<SubtaskDto>,
		val done: Int,
		val undone: Int
)

class SubtaskDto(
		val id: Long,
		val title: String,
		val done: Boolean
)