package akuchars.motivation.infrastructure.repository

import akuchars.motivation.domain.MotivationAddressBookRepository
import akuchars.motivation.domain.model.MotivationAddressBook
import akuchars.motivation.domain.model.NotificationType.EMAIL
import akuchars.motivation.domain.model.StaticMotivationConfigurationSource
import org.springframework.stereotype.Repository

@Repository
// todo akuchars podpiąć tutaj bazę danych
class StaticMotivationAddressBookRepository : MotivationAddressBookRepository {

	override fun findAll(): List<MotivationAddressBook> {
		return listOf(
				MotivationAddressBook(
						"adamkucharski1994@gmail.com",
						EMAIL,
						listOf(
								StaticMotivationConfigurationSource()
						)
				)
		)
	}
}