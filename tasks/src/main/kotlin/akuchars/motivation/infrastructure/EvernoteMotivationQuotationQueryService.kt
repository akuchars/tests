package akuchars.motivation.infrastructure

import akuchars.motivation.application.model.MotivationsQuotationDto
import akuchars.motivation.application.query.MotivationQuotationQueryService
import akuchars.motivation.domain.model.MotivationConfigurationSource
import akuchars.motivation.domain.model.MotivationSourceType
import akuchars.motivation.domain.model.MotivationSourceType.EVERNOTE
import org.springframework.stereotype.Service

@Service
class EvernoteMotivationQuotationQueryService : MotivationQuotationQueryService {

	// todo akuchars dorobić zaczytywanie cytatów z evernote
	override fun fetchMotivationsQuote(motivationConfigurationSource: MotivationConfigurationSource): MotivationsQuotationDto = MotivationsQuotationDto.empty()

	override fun sourceType(): MotivationSourceType = EVERNOTE
}