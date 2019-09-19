package akuchars.domain.task.repository

import akuchars.domain.task.model.Project
import akuchars.domain.task.model.Task
import akuchars.domain.task.model.TaskPriority
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : PagingAndSortingRepository<Task, Long>, CustomTaskRepository {
	fun findAllByPriority(pageable: Pageable, priority: TaskPriority): Page<Task>
	fun findByParent(pageable: Pageable, parent: Project?): Page<Task>
}