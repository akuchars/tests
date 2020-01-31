package akuchars.task.domain.repository

import akuchars.task.domain.exceptions.CannotAddTaskToProjectException
import akuchars.task.domain.exceptions.DomainTaskException
import akuchars.task.domain.model.Project
import akuchars.task.domain.model.Task
import io.vavr.control.Either
import io.vavr.control.Either.left
import io.vavr.control.Either.right

@FunctionalInterface
abstract class AddTaskToProjectPolicy {

	fun canAddTaskToProject(task: Task, project: Project): Either<out DomainTaskException, Task> {
		return if (canAddTaskToProjectInner(task, project)) right(task)
		else left(CannotAddTaskToProjectException(project, task))
	}

	protected abstract fun canAddTaskToProjectInner(task: Task, project: Project): Boolean
}