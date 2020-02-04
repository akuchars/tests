package akuchars.motivation.infrastructure

import akuchars.motivation.application.model.MotivationsQuotationDto
import akuchars.motivation.application.query.MotivationQuotationQueryService
import akuchars.motivation.domain.model.MotivationConfigurationSource
import akuchars.motivation.domain.model.MotivationSourceType
import akuchars.motivation.domain.model.MotivationSourceType.STATIC
import org.springframework.stereotype.Service

@Service
class StaticMotivationQuotationQueryService : MotivationQuotationQueryService {

	override fun fetchMotivationsQuote(motivationConfigurationSource: MotivationConfigurationSource): MotivationsQuotationDto {
		return MotivationsQuotationDto.create(
				"If You can dream it, than You can do it",
				"Każdy chciałby pójść do nieba, nikt nie chce umrzeć"
		)
	}

	override fun sourceType(): MotivationSourceType = STATIC
}