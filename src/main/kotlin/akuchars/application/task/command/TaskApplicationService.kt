package akuchars.application.task.command

import akuchars.application.common.model.FrontDto
import akuchars.application.task.model.PeriodDto
import akuchars.application.task.model.TaskDto
import akuchars.application.task.model.TaskForm
import akuchars.application.task.model.TaskPriorityDto
import akuchars.application.user.query.UserQueryService
import akuchars.domain.common.EventBus
import akuchars.domain.task.model.PeriodOfTime
import akuchars.domain.task.model.Task
import akuchars.domain.task.model.TaskContent
import akuchars.domain.task.model.TaskPriority
import akuchars.domain.task.model.TaskPriority.HIGH
import akuchars.domain.task.model.TaskPriority.LOW
import akuchars.domain.task.model.TaskPriority.MEDIUM
import akuchars.domain.task.model.TaskTitle
import akuchars.domain.task.repository.ChangePeriodAttributePolicy
import akuchars.domain.task.repository.ChangeTaskAssigneeAttributePolicy
import akuchars.domain.task.repository.ProjectRepository
import akuchars.domain.task.repository.ProjectTaskRepository
import akuchars.domain.user.repository.UserRepository
import akuchars.kernel.toFrontDto
import akuchars.kernel.toLocalTime
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Service
class TaskApplicationService(
		private val eventBus: EventBus,
		private val taskRepository: ProjectTaskRepository,
		private val projectRepository: ProjectRepository,
		private val userRepository: UserRepository,
		private val userQueryService: UserQueryService,

		private val changeTaskAssigneePolicy: ChangeTaskAssigneeAttributePolicy,
		private val changePeriodAttributePolicy: ChangePeriodAttributePolicy
) {

	@Transactional
	fun createNewTask(taskForm: TaskForm): FrontDto<TaskDto> {
		val actualUser = userQueryService.getLoggedUser().id.let {
			userRepository.findByIdOrNull(it)
		}!!
		val project = projectRepository.findById(taskForm.projectId).orElseThrow(::RuntimeException)
		return toFrontDto {
			val task = Task.createProjectTask(actualUser, actualUser,
					TaskContent(taskForm.content), TaskTitle(taskForm.title), taskForm.priority.toEntity()
			)
			taskForm.period?.also {
				task.changePeriod(eventBus, changePeriodAttributePolicy, PeriodOfTime(it.start.toLocalTime(), it.end.toLocalTime()))
			}

			project.addTask(eventBus, taskRepository, task) { _, _ -> true }
			task.toDto()
		}
	}

	@Transactional
	fun changeAssigneeForTask(taskId: Long, userId: Long): FrontDto<TaskDto> {
		return toFrontDto {
			val task = taskRepository.findByIdOrNull(taskId) ?: throw EntityNotFoundException()
			val user = userRepository.findByIdOrNull(userId) ?: throw EntityNotFoundException()
			task.changeAssignee(eventBus, changeTaskAssigneePolicy, user).toDto()
		}
	}
}

fun Task.toDto(): TaskDto {
	return TaskDto(id, taskContent.value, taskTitle.value, priority.toDto(),
			assignee.email.value, period?.let { PeriodDto(it.startDate.toString(), it.endDate?.toString()) })
}

fun TaskPriority.toDto(): TaskPriorityDto {
	return when (this) {
		HIGH -> TaskPriorityDto.HIGH
		MEDIUM -> TaskPriorityDto.MEDIUM
		LOW -> TaskPriorityDto.LOW
	}
}
