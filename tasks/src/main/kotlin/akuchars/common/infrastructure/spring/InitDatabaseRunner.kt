package akuchars.common.infrastructure.spring

import akuchars.common.domain.EventBus
import akuchars.common.kernel.ProfileProperties
import akuchars.task.domain.model.Project
import akuchars.task.domain.model.ProjectName
import akuchars.task.domain.model.Task
import akuchars.task.domain.model.TaskContent
import akuchars.task.domain.model.TaskPriority.HIGH
import akuchars.task.domain.model.TaskTitle
import akuchars.task.domain.repository.ProjectRepository
import akuchars.task.domain.repository.TaskRepository
import akuchars.user.domain.model.Email
import akuchars.user.domain.model.Password
import akuchars.user.domain.model.PhoneNumber
import akuchars.user.domain.model.User
import akuchars.user.domain.model.UserData
import akuchars.user.domain.repository.RoleRepository
import akuchars.user.domain.repository.UserRepository
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