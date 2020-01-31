package akuchars.task.application.command

import akuchars.task.domain.model.Task
import akuchars.task.domain.repository.TaskRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class SchedulingTaskService(
		private val taskRepository: TaskRepository
) : ApplicationRunner {
	override fun run(args: ApplicationArguments) {
		calculateNewPeriods()
	}

	@Transactional
	@Scheduled(cron = "0 0 0 * * *")
	fun calculateNewPeriods(): List<Task> {
		return taskRepository.findAllByPeriodNotNull()
				.filter { it.period!!.startDate < LocalDateTime.now() }
				.map { it.recalculatePeriod() }
				.map { taskRepository.save(it) }
	}
}