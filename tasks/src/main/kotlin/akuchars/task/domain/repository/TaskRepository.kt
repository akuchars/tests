package akuchars.task.domain.repository

import akuchars.task.domain.model.Project
import akuchars.task.domain.model.Task
import akuchars.task.domain.model.TaskPriority
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : PagingAndSortingRepository<Task, Long>, CustomTaskRepository {
	fun findAllByPriority(pageable: Pageable, priority: TaskPriority): Page<Task>
	fun findByParent(pageable: Pageable, parent: Project?): Page<Task>
	fun findAllByPeriodNotNull(): List<Task>
}