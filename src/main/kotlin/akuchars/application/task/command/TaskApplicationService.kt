package akuchars.application.task.command

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
import akuchars.domain.task.repository.ProjectRepository
import akuchars.domain.task.repository.ProjectTaskRepository
import akuchars.domain.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TaskApplicationService(
		private val eventBus: EventBus,
		private val taskRepository: ProjectTaskRepository,
		private val projectRepository: ProjectRepository,
		private val userRepository: UserRepository,
		private val userQueryService: UserQueryService
) {

	@Transactional
	fun createNewTask(taskForm: TaskForm): TaskDto {
		val actualUser = userQueryService.getLoggedUser().id.let {
			userRepository.findByIdOrNull(it)
		}!!
		val project = projectRepository.findById(taskForm.projectId).orElseThrow(::RuntimeException)

		return Task.createProjectTask(eventBus, taskRepository, actualUser, actualUser,
				TaskContent(taskForm.content), TaskTitle(taskForm.title), taskForm.priority.toEntity(), project
		).toDto()
	}

	private fun Task.toDto(): TaskDto {
		return TaskDto(this.taskContent.value, this.taskTitle.value, this.priority.toDto(), this.parent.id)
	}

	private fun TaskPriority.toDto(): TaskPriorityDto {
		return when(this) {
			HIGH -> TaskPriorityDto.HIGH
			MEDIUM ->  TaskPriorityDto.MEDIUM
			LOW ->  TaskPriorityDto.LOW
		}
	}
}
