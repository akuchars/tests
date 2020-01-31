package akuchars.task.application.query

import akuchars.common.application.command.FrontDtoConverter
import akuchars.common.application.model.FrontDto
import akuchars.common.infrastructure.spring.SecurityContextUserHolder
import akuchars.task.application.command.toDto
import akuchars.task.application.model.TaskDto
import akuchars.task.application.model.TaskPriorityDto
import akuchars.task.domain.repository.ProjectRepository
import akuchars.task.domain.repository.TagRepository
import akuchars.task.domain.repository.TaskRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TaskQueryService(
		private val taskRepository: TaskRepository,
		private val projectRepository: ProjectRepository,
		private val tagRepository: TagRepository,
		private val frontDtoConverter: FrontDtoConverter
) {

	@Transactional(readOnly = true)
	fun findTasksByTag(tagName: String, projectId: Long, pageable: Pageable): FrontDto<Page<TaskDto>> {
		val project = projectRepository.findByIdAndOwner(projectId, SecurityContextUserHolder.loggedUser)
		return frontDtoConverter.toFrontDto {
			tagRepository.findByIdOrName(null, tagName)
					.map { tag -> taskRepository.findByTagAndProject(pageable, tag, project) }
					.orElse(Page.empty(pageable)).map { it.toDto() }
		}
	}

	@Transactional(readOnly = true)
	fun findTasksByPriority(priorityValue: TaskPriorityDto, projectId: Long, pageable: Pageable): FrontDto<Page<TaskDto>> {
		val project = projectRepository.findByIdAndOwner(projectId, SecurityContextUserHolder.loggedUser)
		return frontDtoConverter.toFrontDto {
			taskRepository.findAllByPriority(pageable, priorityValue.toEntity())
					.map { it.toDto() }
		}
	}

	@Transactional(readOnly = true)
	fun findTaskByProject(projectId: Long, pageable: Pageable): FrontDto<Page<TaskDto>> {
		val project = projectRepository.findByIdAndOwner(projectId, SecurityContextUserHolder.loggedUser)
		return frontDtoConverter.toFrontDto {
			taskRepository.findByParent(pageable, project).map { it.toDto() }
		}
	}
}