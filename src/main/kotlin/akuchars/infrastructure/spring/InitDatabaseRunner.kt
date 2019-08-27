package akuchars.infrastructure.spring

import akuchars.domain.common.EventBus
import akuchars.domain.task.model.Project
import akuchars.domain.task.model.ProjectName
import akuchars.domain.task.model.Task
import akuchars.domain.task.model.TaskContent
import akuchars.domain.task.model.TaskPriority.HIGH
import akuchars.domain.task.model.TaskTitle
import akuchars.domain.task.repository.ProjectRepository
import akuchars.domain.task.repository.ProjectTaskRepository
import akuchars.domain.user.model.User
import akuchars.domain.user.repository.UserRepository
import akuchars.kernel.ProfileProperties
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Profile(ProfileProperties.H2_PROFILE)
class InitDatabaseRunner (
		private val userRepository: UserRepository,
		private val projectRepository: ProjectRepository,
		private val taskRepository: ProjectTaskRepository,
		private val mockedEventBus: EventBus
): ApplicationRunner {

	@Transactional
	override fun run(args: ApplicationArguments) {
		val user = userRepository.save(User())

		val project = Project.createProject(mockedEventBus, projectRepository, ProjectName("Some nice project name"), user)

		Task.createProjectTask(mockedEventBus, taskRepository, user, user, TaskContent("My task content"), TaskTitle("Some title"), HIGH, project)
	}
}