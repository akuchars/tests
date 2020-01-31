package akuchars.task.domain.repository

import akuchars.task.domain.model.Subtask
import akuchars.task.domain.model.Task
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SubtaskRepository: CrudRepository<Subtask, Long> {
	fun findByParentAndId(parent: Task, id: Long): Subtask
}