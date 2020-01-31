package akuchars.motivation.infrastructure

import akuchars.motivation.application.model.MotivationsQuotationDto
import akuchars.motivation.application.query.MotivationQuotationQueryService
import org.springframework.stereotype.Service

@Service
class HibernateMotivationQuotationQueryService : MotivationQuotationQueryService {

	// todo akuchars - dorobić tutaj branie z bazy danych
	override fun fetchMotivationsQuote(): MotivationsQuotationDto = MotivationsQuotationDto.empty()
}