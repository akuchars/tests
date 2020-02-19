package akuchars.common.infrastructure.spring

import akuchars.common.domain.EventBus
import akuchars.common.kernel.ProfileProperties
import akuchars.motivation.domain.AddressBookRepository
import akuchars.motivation.domain.QuotationRepository
import akuchars.motivation.domain.model.AddressBook
import akuchars.motivation.domain.model.NotificationType.EMAIL
import akuchars.motivation.domain.model.Quotation
import akuchars.motivation.domain.model.SourceQuotation
import akuchars.motivation.domain.model.SourceType.STATIC_TEXT
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
		private val passwordEncoder: PasswordEncoder,
		private val quotationRepository: QuotationRepository,
		private val addressBookRepository: AddressBookRepository
) : ApplicationRunner {

	@Transactional
	override fun run(args: ApplicationArguments) {
//		val user = createMe()
//
//		val project = Project.createProject(mockedEventBus, projectRepository, ProjectName("Some nice project name"), user)
//
//		val task = Task.createProjectTask(user, user, TaskContent("My task content"), TaskTitle("Some title"), HIGH)
//		project.addTask(mockedEventBus, taskRepository, task) { _, _ -> true }
		val motivations = createMotivations()
		motivations.forEach {
			quotationRepository.save(it)
		}

		val book = AddressBook("adamkucharski1994@gmail.com", EMAIL).apply {
			this.motivations = motivations.map { SourceQuotation(it, this, true, STATIC_TEXT) }
					.toMutableList()
		}
		addressBookRepository.save(book)
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

	private fun createMotivations() =
			listOf(
					"Wszystko zaczyna się od marzeń. ",
					"Najważniejsze jest najbliższe 5 minut, reszta to wyobraźnia.",
					"If You Can dream It, than You can do It. ",
					"There is difference beetwen knowing the path and walking the path",
					"Zanim wygrasz daj sobie szansę że przegrasz",
					"Przyszłość zaczyna się dziś, nie jutro",
					"Twoje przekonania stają się twoimi myślami, \n" +
							"Twoje myśli stają się twoimi słowami, \n" +
							"Twoje słowa stają się twoimi czynami, \n" +
							"Twoje czyny stają się twoimi zwyczajami, \n" +
							"Twoje zwyczaje stają się twoimi wartościami, \n" +
							"A twoje wartości stają się twoim przeznaczeniem.",
					"Udawaj tak długo, aż Ci się powiedzie",
					"Upadnij siedem razy, wstań po raz ósmy.",
					"Wszyscy jesteśmy tym, co w swoim życiu powtarzamy. Dlatego doskonałość nie jest jednorazowym aktem, lecz nawykiem.",
					"Każdy chce iść do nieba, nikt nie chce umrzeć",
					"Dostaniesz dokładnie tyle, ile z siebie dasz.",
					"To, co zasiejesz wiosną, zbierzesz latem podczas żniw.",
					"Z czym byś się obudził jutro rano, gdybyś się obudził tylko z tym za co podziękowałeś dzisiaj",
					"Tchórz umiera tysiąc razy, człowiek odważny umiera tylko raz",
					"Wymagajcie od siebie, choćby inni od Was nie wymagali"
			).map { Quotation(it, "") }
}