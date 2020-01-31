package akuchars.motivation.infrastructure

import akuchars.motivation.application.model.MotivationsQuotationDto
import akuchars.motivation.application.query.MotivationQuotationQueryService
import org.springframework.stereotype.Service

@Service
class StaticMotivationQuotationQueryService : MotivationQuotationQueryService {

	override fun fetchMotivationsQuote(): MotivationsQuotationDto {
		return MotivationsQuotationDto.create(
				"If You can dream it, than You can do it",
				"Każdy chciałby pójść do nieba, nikt nie chce umrzeć"
		)
	}
}