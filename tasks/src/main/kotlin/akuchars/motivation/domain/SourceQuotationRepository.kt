package akuchars.motivation.domain

import akuchars.motivation.domain.model.AddressBook
import akuchars.motivation.domain.model.SourceQuotation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SourceQuotationRepository : CrudRepository<SourceQuotation, Long> {
	fun findAllByAddressBook(pageable: Pageable, addressBook: AddressBook?): Page<SourceQuotation>
	fun findAllByAddressBook(addressBook: AddressBook?): SourceQuotation?
	fun getById(id: Long): SourceQuotation
}