package akuchars.motivation.infrastructure

import akuchars.motivation.application.model.MotivationsQuotationDto
import akuchars.motivation.application.query.MotivationQuotationQueryService
import org.springframework.stereotype.Service

@Service
class EvernoteMotivationQuotationQueryService : MotivationQuotationQueryService {

	// todo akuchars dorobić zaczytywanie cytatów z evernote
	override fun fetchMotivationsQuote(): MotivationsQuotationDto = MotivationsQuotationDto.empty()
}