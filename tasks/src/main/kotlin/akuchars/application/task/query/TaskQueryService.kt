package akuchars.application.task.query

import akuchars.application.common.command.FrontDtoConverter
import akuchars.application.common.model.FrontDto
import akuchars.application.task.command.toDto
import akuchars.application.task.model.TaskDto
import akuchars.application.task.model.TaskPriorityDto
import akuchars.domain.task.repository.ProjectRepository
import akuchars.domain.task.repository.TagRepository
import akuchars.domain.task.repository.TaskRepository
import akuchars.infrastructure.spring.SecurityContextUserHolder
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