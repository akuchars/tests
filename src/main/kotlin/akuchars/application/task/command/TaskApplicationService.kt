package akuchars.application.task.command

import akuchars.application.common.command.FrontDtoConverter
import akuchars.application.common.model.FrontDto
import akuchars.application.task.model.PeriodDto
import akuchars.application.task.model.TagDto
import akuchars.application.task.model.TaskDto
import akuchars.application.task.model.TaskForm
import akuchars.application.task.model.TaskPriorityDto
import akuchars.application.user.query.UserQueryService
import akuchars.domain.common.EventBus
import akuchars.domain.task.model.PeriodOfTime
import akuchars.domain.task.model.Task
import akuchars.domain.task.model.TaskContent
import akuchars.domain.task.model.TaskMainGoal
import akuchars.domain.task.model.TaskPriority
import akuchars.domain.task.model.TaskPriority.HIGH
import akuchars.domain.task.model.TaskPriority.LOW
import akuchars.domain.task.model.TaskPriority.MEDIUM
import akuchars.domain.task.model.TaskTitle
import akuchars.domain.task.repository.ChangePeriodAttributePolicy
import akuchars.domain.task.repository.ChangeTaskAssigneeAttributePolicy
import akuchars.domain.task.repository.ProjectRepository
import akuchars.domain.task.repository.TaskRepository
import akuchars.domain.user.model.User
import akuchars.domain.user.repository.UserRepository
import akuchars.infrastructure.task.DefaultAddTagToTaskAttributePolicy
import akuchars.kernel.toLocalTime
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Service
class TaskApplicationService(
		private val eventBus: EventBus,
		private val taskRepository: TaskRepository,
		private val projectRepository: ProjectRepository,
		private val userRepository: UserRepository,
		private val userQueryService: UserQueryService,
		private val tagApplicationService: TagApplicationService,

		private val changeTaskAssigneePolicy: ChangeTaskAssigneeAttributePolicy,
		private val changePeriodAttributePolicy: ChangePeriodAttributePolicy,
		private val frontDtoConverter: FrontDtoConverter
) {

	@Transactional
	fun createNewTask(taskForm: TaskForm): FrontDto<TaskDto> {
		return createOrEditTask(taskForm) { taskFormInner, actualUser ->
			Task.createProjectTask(actualUser, actualUser,
					TaskContent(taskFormInner.content), TaskTitle(taskFormInner.title), taskFormInner.priority.toEntity()
			)
		}
	}

	@Transactional
	fun editTask(taskForm: TaskForm): FrontDto<TaskDto> {
		return createOrEditTask(taskForm) { taskFormInner, actualUser ->
			taskRepository.findByIdOrNull(taskFormInner.id!!)!!
					.changeTaskData(eventBus,
							TaskContent(taskFormInner.content),
							TaskTitle(taskFormInner.title),
							taskFormInner.priority.toEntity()
					)
		}
	}

	private fun createOrEditTask(taskForm: TaskForm, taskCreator: (TaskForm, User) -> Task): FrontDto<TaskDto> {
		val actualUser = userQueryService.getLoggedUser().id.let {
			userRepository.findByIdOrNull(it)
		}!!
		val project = projectRepository.findById(taskForm.projectId).orElseThrow(::RuntimeException)
		return frontDtoConverter.toFrontDto {
			val task = taskCreator.invoke(taskForm, actualUser)
			taskForm.period?.also {
				task.changePeriod(eventBus, changePeriodAttributePolicy, PeriodOfTime(it.start.toLocalTime(), it.end.toLocalTime()))
			}
			taskForm.mainGoal?.also {
				task.mainGoal = TaskMainGoal(it)
			}
			tagApplicationService.findOrCreateTags(taskForm).forEach { tag ->
				task.addTag(eventBus, DefaultAddTagToTaskAttributePolicy(), tag)
			}
			project.addTask(eventBus, taskRepository, task) { _, _ -> true }
			task.toDto()
		}
	}

	@Transactional
	fun changeAssigneeForTask(taskId: Long, userId: Long): FrontDto<TaskDto> {
		return frontDtoConverter.toFrontDto {
			val task = taskRepository.findByIdOrNull(taskId) ?: throw EntityNotFoundException()
			val user = userRepository.findByIdOrNull(userId) ?: throw EntityNotFoundException()
			task.changeAssignee(eventBus, changeTaskAssigneePolicy, user).toDto()
		}
	}
}

fun Task.toDto(): TaskDto {
	return TaskDto(id,
			taskContent.value, taskTitle.value, priority.toDto(),
			assignee.email.value,
			period?.let { PeriodDto(it.startDate.toString(), it.endDate?.toString()) },
			mainGoal?.value,
			tags.map { TagDto(it.id, it.name) }.toSet()
	)
}

fun TaskPriority.toDto(): TaskPriorityDto {
	return when (this) {
		HIGH -> TaskPriorityDto.HIGH
		MEDIUM -> TaskPriorityDto.MEDIUM
		LOW -> TaskPriorityDto.LOW
	}
}
