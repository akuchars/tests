package akuchars.application.task.command

import akuchars.application.task.model.ProjectDto
import akuchars.application.task.model.TaskDto
import akuchars.application.user.model.UserDto
import akuchars.application.user.query.UserQueryService
import akuchars.domain.common.EventBus
import akuchars.domain.task.model.Project
import akuchars.domain.task.model.ProjectName
import akuchars.domain.task.repository.ProjectRepository
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProjectApplicationService(
		private val eventBus: EventBus,
		private val projectRepository: ProjectRepository,
		private val userQueryService: UserQueryService
) {

	@Transactional
	fun createNewProject(projectName: String): ProjectDto {
		val actualUser = userQueryService.getLoggedUser()
		return Project.createProject(eventBus, projectRepository, ProjectName(projectName), actualUser).convertToDto()
	}

	@Transactional(readOnly = true)
	fun getProjectById(id: Long): ProjectDto? =
			projectRepository.findByIdOrNull(id)?.convertToDto()

	@Transactional
	fun getProjectsPaginated(pageable: Pageable) =
			projectRepository.findAll(pageable).map { it.convertToDto() }

	private fun Project.convertToDto(): ProjectDto {
		return ProjectDto(id, name.value, UserDto(), tasks.map { TaskDto() }.toSet())
//			TODO("Poprawić tutaj ")
	}
}