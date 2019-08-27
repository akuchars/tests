package akuchars.domain.task.repository

import akuchars.domain.task.model.Task
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectTaskRepository : CrudRepository<Task, Long>