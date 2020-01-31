package akuchars.task.application.command

import akuchars.common.application.command.FrontDtoConverter
import akuchars.common.application.model.FrontDto
import akuchars.common.domain.EventBus
import akuchars.common.infrastructure.spring.SecurityContextUserHolder
import akuchars.task.application.model.DetailProjectDto
import akuchars.task.application.model.ProjectDto
import akuchars.task.domain.model.Project
import akuchars.task.domain.model.ProjectName
import akuchars.task.domain.repository.ProjectRepository
import akuchars.user.application.model.FrontUserDto
import akuchars.user.application.query.UserQueryService
import akuchars.user.domain.model.User
import akuchars.user.domain.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProjectApplicationService(
		private val eventBus: EventBus,
		private val projectRepository: ProjectRepository,
		private val userRepository: UserRepository,
		private val userQueryService: UserQueryService,
		private val frontDtoConverter: FrontDtoConverter
) {

	@Transactional
	fun createNewProject(projectName: String): FrontDto<ProjectDto> {
		return frontDtoConverter.toFrontDto { Project.createProject(eventBus, projectRepository, ProjectName(projectName), getUser()).convertToDto() }
	}

	@Transactional(readOnly = true)
	fun findProjectById(id: Long): DetailProjectDto? =
			projectRepository.findByIdAndOwner(id, getUser())?.convertToDetailDto()

	@Transactional(readOnly = true)
	fun getProjectsPaginated(pageable: Pageable): Page<ProjectDto> =
			projectRepository.findAllByOwner(pageable, getUser()).map { it.convertToDto() }

	private fun getUser(): User = SecurityContextUserHolder.loggedUser
}

fun Project.convertToDto(): ProjectDto =
		ProjectDto(id, name.value, tasks.map { it.toDto() }.toSet())

//todo tutaj będzie można dodać odrazu grupowanie tasków po priorytetach/datach/statusach
fun Project.convertToDetailDto(): DetailProjectDto =
		DetailProjectDto(id, name.value, tasks.map { it.toDto() }.toSet(),
				users.map { FrontUserDto(it.id, it.userData.name, it.userData.surname, it.email.value) }.toSet())