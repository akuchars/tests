package akuchars.task.application.command

import akuchars.common.application.command.FrontDtoConverter
import akuchars.common.application.model.FrontDto
import akuchars.common.domain.EventBus
import akuchars.common.kernel.toLocalTime
import akuchars.task.application.model.PeriodDto
import akuchars.task.application.model.SubtaskDto
import akuchars.task.application.model.SubtaskInfoDto
import akuchars.task.application.model.TagDto
import akuchars.task.application.model.TaskDto
import akuchars.task.application.model.TaskForm
import akuchars.task.application.model.TaskPriorityDto
import akuchars.task.application.model.TaskStatusDto
import akuchars.task.domain.model.PeriodOfTime
import akuchars.task.domain.model.Subtask
import akuchars.task.domain.model.Task
import akuchars.task.domain.model.TaskContent
import akuchars.task.domain.model.TaskMainGoal
import akuchars.task.domain.model.TaskPriority
import akuchars.task.domain.model.TaskPriority.HIGH
import akuchars.task.domain.model.TaskPriority.LOW
import akuchars.task.domain.model.TaskPriority.MEDIUM
import akuchars.task.domain.model.TaskStatus
import akuchars.task.domain.model.TaskStatus.DONE
import akuchars.task.domain.model.TaskStatus.NEW
import akuchars.task.domain.model.TaskTitle
import akuchars.task.domain.repository.ChangePeriodAttributePolicy
import akuchars.task.domain.repository.ChangeTaskAssigneeAttributePolicy
import akuchars.task.domain.repository.ProjectRepository
import akuchars.task.domain.repository.SubtaskRepository
import akuchars.task.domain.repository.TaskRepository
import akuchars.task.infrastructure.DefaultAddTagToTaskAttributePolicy
import akuchars.task.infrastructure.DefaultFinishSubtaskPolicy
import akuchars.user.application.query.UserQueryService
import akuchars.user.domain.model.User
import akuchars.user.domain.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Service
class TaskApplicationService(
		private val eventBus: EventBus,
		private val taskRepository: TaskRepository,
		private val subtaskRepository: SubtaskRepository,
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

	@Transactional
	fun finishSubtask(taskId: Long, subtaskId: Long): FrontDto<TaskDto> {
		return frontDtoConverter.toFrontDto {
			val task = taskRepository.findById(taskId).orElseThrow(::RuntimeException)
			val subtask = subtaskRepository.findByParentAndId(task, subtaskId)
			task.finishSubtask(eventBus, subtask, DefaultFinishSubtaskPolicy()).toDto()
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
			task.addSubtasks(eventBus, taskForm.subtasks.map { Subtask(TaskTitle(it.title), NEW, task) }.toMutableList())

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
			tags.map { TagDto(it.id, it.name) }.toSet(),
			SubtaskInfoDto(
					subtasks.map { SubtaskDto(it.id, it.taskTitle.value, it.isDone()) },
					countDoneSubtask(),
					countUndoneSubtask()
					),
			status.toDto()
	)
}

private fun TaskStatus.toDto(): TaskStatusDto {
	return when(this) {
		NEW -> TaskStatusDto.NEW
		DONE -> TaskStatusDto.DONE
	}
}

fun TaskPriority.toDto(): TaskPriorityDto {
	return when (this) {
		HIGH -> TaskPriorityDto.HIGH
		MEDIUM -> TaskPriorityDto.MEDIUM
		LOW -> TaskPriorityDto.LOW
	}
}
