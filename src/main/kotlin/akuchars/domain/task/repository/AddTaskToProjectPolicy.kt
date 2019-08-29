package akuchars.domain.task.repository

import akuchars.domain.task.model.Project
import akuchars.domain.task.model.Task

interface AddTaskToProjectPolicy {

	fun canAddTaskToProject(task: Task, project: Project): Boolean {
		return canAddTaskToProjectInner(task, project)
	}

	fun canAddTaskToProjectInner(task: Task, project: Project): Boolean
}