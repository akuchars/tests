package akuchars.domain.task.repository

import akuchars.domain.task.model.Task
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : PagingAndSortingRepository<Task, Long> , CustomTaskRepository