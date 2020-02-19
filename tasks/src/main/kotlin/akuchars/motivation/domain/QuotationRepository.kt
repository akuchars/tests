package akuchars.motivation.domain

import akuchars.motivation.domain.model.Quotation
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface QuotationRepository : CrudRepository<Quotation, Long> {
	fun getById(id: Long): Quotation
}