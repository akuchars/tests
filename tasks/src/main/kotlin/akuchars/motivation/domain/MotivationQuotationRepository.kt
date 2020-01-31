package akuchars.motivation.domain

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface MotivationQuotationRepository : CrudRepository<MotivationQuotation, Long>