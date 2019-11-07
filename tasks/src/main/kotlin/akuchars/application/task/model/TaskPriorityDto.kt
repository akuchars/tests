package akuchars.application.task.model

import akuchars.domain.task.model.TaskPriority

enum class TaskPriorityDto {
	HIGH,
	MEDIUM,
	LOW;

	fun toEntity(): TaskPriority {
		return when(this) {
			HIGH -> TaskPriority.HIGH
			MEDIUM -> TaskPriority.MEDIUM
			LOW -> TaskPriority.LOW
		}
	}
}