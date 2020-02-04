package akuchars.motivation.application.query

import akuchars.motivation.application.model.MotivationsQuotationDto
import akuchars.motivation.domain.model.MotivationConfigurationSource
import akuchars.motivation.domain.model.MotivationSourceType

interface MotivationQuotationQueryService {

	fun fetchMotivationsQuote(motivationConfigurationSource: MotivationConfigurationSource): MotivationsQuotationDto

	fun sourceType(): MotivationSourceType
}