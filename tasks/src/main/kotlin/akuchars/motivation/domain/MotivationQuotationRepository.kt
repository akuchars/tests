package akuchars.motivation.domain

import akuchars.motivation.domain.model.MotivationQuotation
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MotivationQuotationRepository : CrudRepository<MotivationQuotation, Long>