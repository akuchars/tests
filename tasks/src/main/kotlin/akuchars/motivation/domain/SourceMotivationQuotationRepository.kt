package akuchars.motivation.domain

import akuchars.motivation.domain.model.MotivationAddressBook
import akuchars.motivation.domain.model.SourceMotivationQuotation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SourceMotivationQuotationRepository : CrudRepository<SourceMotivationQuotation, Long> {
	fun findAllByAddressBook(pageable: Pageable, addressBook: MotivationAddressBook?): Page<SourceMotivationQuotation>
}