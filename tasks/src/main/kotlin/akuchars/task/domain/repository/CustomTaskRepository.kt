package akuchars.task.domain.repository

import akuchars.task.domain.model.Project
import akuchars.task.domain.model.Tag
import akuchars.task.domain.model.Task
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CustomTaskRepository {
	fun findByTagAndProject(pageable: Pageable, tag: Tag, project: Project?): Page<Task>
}