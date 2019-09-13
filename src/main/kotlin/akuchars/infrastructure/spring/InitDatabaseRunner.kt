package akuchars.infrastructure.spring

import akuchars.domain.common.EventBus
import akuchars.domain.task.model.Project
import akuchars.domain.task.model.ProjectName
import akuchars.domain.task.model.Task
import akuchars.domain.task.model.TaskContent
import akuchars.domain.task.model.TaskPriority.HIGH
import akuchars.domain.task.model.TaskTitle
import akuchars.domain.task.repository.ProjectRepository
import akuchars.domain.task.repository.TaskRepository
import akuchars.domain.user.model.Email
import akuchars.domain.user.model.Password
import akuchars.domain.user.model.PhoneNumber
import akuchars.domain.user.model.User
import akuchars.domain.user.model.UserData
import akuchars.domain.user.repository.RoleRepository
import akuchars.domain.user.repository.UserRepository
import akuchars.kernel.ProfileProperties
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Profile(ProfileProperties.H2_PROFILE)
class InitDatabaseRunner(
		private val userRepository: UserRepository,
		private val projectRepository: ProjectRepository,
		private val taskRepository: TaskRepository,
		private val roleRepository: RoleRepository,
		private val mockedEventBus: EventBus,
		private val passwordEncoder: PasswordEncoder
) : ApplicationRunner {

	@Transactional
	override fun run(args: ApplicationArguments) {
		val user = createMe()

		val project = Project.createProject(mockedEventBus, projectRepository, ProjectName("Some nice project name"), user)

		val task = Task.createProjectTask(user, user, TaskContent("My task content"), TaskTitle("Some title"), HIGH)
		project.addTask(mockedEventBus, taskRepository, task) { _, _ -> true }
	}

	private fun createMe(): User {
		return User.createUser(
				mockedEventBus,
				userRepository,
				UserData("Adam", "Kucharski"),
				Email("adamkucharski1994@gmail.com"),
				Password(passwordEncoder.encode("haslo123")),
				roleRepository.findAll().toSet(),
				PhoneNumber("501464579")
		)
	}
}