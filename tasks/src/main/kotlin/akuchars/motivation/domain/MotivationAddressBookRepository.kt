package akuchars.motivation.domain

interface MotivationAddressBookRepository {

	fun findAll(): List<MotivationAddressBook>
}