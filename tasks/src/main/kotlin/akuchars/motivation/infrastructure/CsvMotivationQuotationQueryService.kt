package akuchars.motivation.infrastructure

import akuchars.motivation.application.model.MotivationsQuotationDto
import akuchars.motivation.application.query.MotivationQuotationQueryService
import org.springframework.stereotype.Service

@Service
class CsvMotivationQuotationQueryService : MotivationQuotationQueryService {

	// todo akuchars dorobić zaczytywanie z csvki
	override fun fetchMotivationsQuote(): MotivationsQuotationDto = MotivationsQuotationDto.empty()
}