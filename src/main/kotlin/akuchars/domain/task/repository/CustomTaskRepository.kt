package akuchars.domain.task.repository

import akuchars.domain.task.model.Project
import akuchars.domain.task.model.Tag
import akuchars.domain.task.model.Task
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomTaskRepository {
	fun findByTagAndProject(pageable: Pageable, tag: Tag, project: Project?): Page<Task>
}