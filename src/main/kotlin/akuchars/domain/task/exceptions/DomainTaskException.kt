package akuchars.domain.task.exceptions

import akuchars.domain.common.DomainException
import akuchars.domain.task.model.Project
import akuchars.domain.task.model.Task
import akuchars.domain.user.model.User

sealed class DomainTaskException(frontErrorCode: String, msg: String): DomainException(frontErrorCode, msg)

class CannotAddTaskToProjectException(val project: Project, val task: Task) : DomainTaskException(
		"task.tasks.cannot.add.task.to.project.error.code",
		"Cannot adding new task ${task.id} to project ${project.name.value}"
)

class CannotChangeAssigneeForTaskException(val user: User, val task: Task) : DomainTaskException(
		"task.tasks.cannot.change.assignee.for.task.error.code",
		"Cannot change assignee for task ${task.id} and user ${user.email}, user is not in project"
)