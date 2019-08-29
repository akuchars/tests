package akuchars.application.task.command

import akuchars.application.task.model.ProjectDto
import akuchars.application.user.query.UserQueryService
import akuchars.domain.common.EventBus
import akuchars.domain.task.model.Project
import akuchars.domain.task.model.ProjectName
import akuchars.domain.task.repository.ProjectRepository
import akuchars.domain.user.model.User
import akuchars.domain.user.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProjectApplicationService(
		private val eventBus: EventBus,
		private val projectRepository: ProjectRepository,
		private val userRepository: UserRepository,
		private val userQueryService: UserQueryService
) {

	@Transactional
	fun createNewProject(projectName: String): ProjectDto =
			Project.createProject(eventBus, projectRepository, ProjectName(projectName), getUser()).convertToDto()

	@Transactional(readOnly = true)
	fun findProjectById(id: Long): ProjectDto? =
			projectRepository.findByIdAndOwner(id, getUser())?.convertToDto()

	@Transactional(readOnly = true)
	fun getProjectsPaginated(pageable: Pageable): Page<ProjectDto> =
			projectRepository.findAllByOwner(pageable, getUser()).map { it.convertToDto() }

	private fun getUser(): User = userQueryService.getLoggedUser().id.let {
		userRepository.findByIdOrNull(it)
	}!!
}

fun Project.convertToDto(): ProjectDto =
		ProjectDto(id, name.value, tasks.map { it.toDto() }.toSet())