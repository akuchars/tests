package akuchars.application.task.command

import akuchars.application.common.model.FrontDto
import akuchars.application.task.model.TaskDto
import akuchars.application.task.model.TaskForm
import akuchars.application.task.model.TaskPriorityDto
import akuchars.application.user.query.UserQueryService
import akuchars.domain.common.EventBus
import akuchars.domain.task.model.Task
import akuchars.domain.task.model.TaskContent
import akuchars.domain.task.model.TaskPriority
import akuchars.domain.task.model.TaskPriority.HIGH
import akuchars.domain.task.model.TaskPriority.LOW
import akuchars.domain.task.model.TaskPriority.MEDIUM
import akuchars.domain.task.model.TaskTitle
import akuchars.domain.task.repository.ChangeTaskAssigneePolicy
import akuchars.domain.task.repository.ProjectRepository
import akuchars.domain.task.repository.ProjectTaskRepository
import akuchars.domain.user.repository.UserRepository
import akuchars.kernel.toFrontDto
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
		private val changeTaskAssigneePolicy: ChangeTaskAssigneePolicy
) {

	@Transactional
	fun createNewTask(taskForm: TaskForm): FrontDto<TaskDto> {
		val actualUser = userQueryService.getLoggedUser().id.let {
			userRepository.findByIdOrNull(it)
		}!!
		val project = projectRepository.findById(taskForm.projectId).orElseThrow(::RuntimeException)

		val task = Task.createProjectTask(actualUser, actualUser,
				TaskContent(taskForm.content), TaskTitle(taskForm.title), taskForm.priority.toEntity()
		)
		return toFrontDto {
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
	return TaskDto(id, this.taskContent.value, this.taskTitle.value, this.priority.toDto(), this.assignee.email.value)
}

fun TaskPriority.toDto(): TaskPriorityDto {
	return when (this) {
		HIGH -> TaskPriorityDto.HIGH
		MEDIUM -> TaskPriorityDto.MEDIUM
		LOW -> TaskPriorityDto.LOW
	}
}
