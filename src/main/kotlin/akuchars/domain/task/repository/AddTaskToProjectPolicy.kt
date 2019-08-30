package akuchars.domain.task.repository

import akuchars.domain.task.exceptions.CannotAddTaskToProjectException
import akuchars.domain.task.exceptions.DomainTaskException
import akuchars.domain.task.model.Project
import akuchars.domain.task.model.Task
import io.vavr.control.Either
import io.vavr.control.Either.left
import io.vavr.control.Either.right

interface AddTaskToProjectPolicy {

	fun canAddTaskToProject(task: Task, project: Project): Either<out DomainTaskException, Task> {
		return if (canAddTaskToProjectInner(task, project)) right(task)
		else left(CannotAddTaskToProjectException(project, task))
	}

	fun canAddTaskToProjectInner(task: Task, project: Project): Boolean
}