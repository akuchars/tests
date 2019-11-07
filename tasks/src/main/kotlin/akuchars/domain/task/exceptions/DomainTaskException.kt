package akuchars.domain.task.exceptions

import akuchars.domain.common.DomainException
import akuchars.domain.task.model.PeriodOfTime
import akuchars.domain.task.model.Project
import akuchars.domain.task.model.Subtask
import akuchars.domain.task.model.Task
import akuchars.domain.user.model.User

sealed class DomainTaskException(frontErrorCode: String, msg: String): DomainException(frontErrorCode, msg)
class CannotAddTaskToProjectException(val project: Project, val task: Task) : DomainTaskException(
		"task.tasks.cannot.add.task.to.project.error.code",
		"Cannot adding new task ${task.id} to project ${project.name.value}"
) {
	override fun getMessageResolverParams(): Array<*>? = arrayOf(task.id, project.name.value)
}

class CannotChangeAssigneeForTaskException(val user: User, val task: Task) : DomainTaskException(
		"task.tasks.cannot.change.assignee.for.task.error.code",
		"Cannot change assignee for task ${task.id} and user ${user.email}, user is not in project"
) {
	override fun getMessageResolverParams(): Array<*>? = arrayOf(task.taskTitle.value, user.email)
}

class CannotChangePeriodOfTaskException(val periodOfTime: PeriodOfTime, val task: Task) : DomainTaskException(
		"task.tasks.cannot.change.period.of.task.error.code",
		"Cannot change period of $periodOfTime time for closed task ${task.id}"
) {
	override fun getMessageResolverParams(): Array<*>? = arrayOf(periodOfTime, task.id)
}

class CannotFinishSubtaskOfTaskException(val subtask: Subtask, val task: Task) : DomainTaskException(
		"task.tasks.cannot.finish.subtask.of.task.error.code",
		"Cannot finish subtask ${subtask.id} of task ${task.id}"
) {
	override fun getMessageResolverParams(): Array<*>? = arrayOf(subtask.id, task.id)
}