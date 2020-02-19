package akuchars.motivation.domain

import akuchars.motivation.domain.model.AddressBook
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AddressBookRepository : CrudRepository<AddressBook, Long> {
	fun findByAddressee(addressee: String): AddressBook?
}