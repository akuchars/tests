package akuchars.task.domain.exceptions

import akuchars.common.domain.DomainException
import akuchars.task.domain.model.PeriodOfTime
import akuchars.task.domain.model.Project
import akuchars.task.domain.model.Subtask
import akuchars.task.domain.model.Task
import akuchars.user.domain.model.User

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