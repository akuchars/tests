package akuchars.motivation.domain

import akuchars.motivation.domain.model.MotivationAddressBook
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MotivationAddressBookRepository : CrudRepository<MotivationAddressBook, Long> {
	fun findByAddressee(addressee: String): MotivationAddressBook?
}