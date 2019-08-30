package akuchars.domain.task.exceptions

import akuchars.domain.task.model.Project
import akuchars.domain.task.model.Task

sealed class DomainTaskException(val frontErrorCode: String, msg: String) : Exception(msg)

class CannotAddTaskToProjectException(val project: Project, val task: Task) : DomainTaskException(
		"task.tasks.cannot.add.task.to.project.error.code",
		"Cannot adding new task ${task.id} to project ${project.name.value}"
)