package akuchars.motivation.application.command

import akuchars.motivation.application.model.MotivationsQuotationDto
import akuchars.motivation.application.query.MotivationQuotationQueryService
import org.springframework.stereotype.Service

@Service
class MotivationQuotationProvider(
		private val motivationQuotationQueryServices: List<MotivationQuotationQueryService>
) {

	fun getAllMotivationQuotation(): MotivationsQuotationDto {
		return motivationQuotationQueryServices
				.map { it.fetchMotivationsQuote() }
				.flatMap { it.motivations }
				.let { MotivationsQuotationDto.copyFrom(it) }
	}

	fun getRandomlyMotivationQuotation() = getAllMotivationQuotation().motivations.shuffled().first()
}