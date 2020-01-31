package akuchars.motivation.infrastructure

import akuchars.motivation.domain.MotivationAddressBook
import akuchars.motivation.domain.MotivationAddressBookRepository
import akuchars.notification.common.domain.NotificationType
import org.springframework.stereotype.Repository

@Repository
// todo akuchars podpiąć tutaj bazę danych
class StaticMotivationAddressBookRepository : MotivationAddressBookRepository {

	override fun findAll(): List<MotivationAddressBook> {
		return listOf(
				MotivationAddressBook("adamkucharski1994@gmail.com", NotificationType.EMAIL)
		)
	}
}