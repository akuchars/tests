package akuchars.motivation.application.query

import akuchars.motivation.application.model.MotivationsQuotationDto

interface MotivationQuotationQueryService {

	fun fetchMotivationsQuote(): MotivationsQuotationDto
}