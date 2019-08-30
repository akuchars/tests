package akuchars.domain.task.repository

import akuchars.domain.task.exceptions.CannotChangeAssigneeForTaskException
import akuchars.domain.task.exceptions.DomainTaskException
import akuchars.domain.task.model.Task
import akuchars.domain.user.model.User
import io.vavr.control.Either

@FunctionalInterface
abstract class ChangeTaskAssigneePolicy {
	fun canChangeAssigneeForTask(task: Task, assignee: User): Either<out DomainTaskException, Task> {
		return if (canChangeAssigneeForTaskInner(task, assignee)) Either.right(task)
		else Either.left(CannotChangeAssigneeForTaskException(assignee, task))
	}

	protected abstract fun canChangeAssigneeForTaskInner(task: Task, assignee: User): Boolean
}