package akuchars.domain.task.repository

import akuchars.domain.task.model.Subtask
import akuchars.domain.task.model.Task
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SubtaskRepository: CrudRepository<Subtask, Long> {
	fun findByParentAndId(parent: Task, id: Long): Subtask
}