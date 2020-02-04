package akuchars.motivation.domain

import akuchars.motivation.domain.model.MotivationAddressBook

interface MotivationAddressBookRepository {

	fun findAll(): List<MotivationAddressBook>
}