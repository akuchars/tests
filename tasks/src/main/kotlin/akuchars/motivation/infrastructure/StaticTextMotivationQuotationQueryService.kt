package akuchars.motivation.infrastructure

import akuchars.motivation.application.model.MotivationQuotationDto
import akuchars.motivation.application.model.MotivationsQuotationDto
import akuchars.motivation.application.query.MotivationQuotationQueryService
import akuchars.motivation.domain.model.MotivationConfigurationSource
import akuchars.motivation.domain.model.MotivationSourceType
import akuchars.motivation.domain.model.MotivationSourceType.STATIC_TEXT
import akuchars.motivation.domain.model.StaticTextMotivationConfigurationSource
import org.springframework.stereotype.Service

@Service
class StaticTextMotivationQuotationQueryService : MotivationQuotationQueryService {

	override fun fetchMotivationsQuote(motivationConfigurationSource: MotivationConfigurationSource): MotivationsQuotationDto {
		motivationConfigurationSource as StaticTextMotivationConfigurationSource
		val split = motivationConfigurationSource.splitRegex
		val splitAuthorText = motivationConfigurationSource.splitAuthorText
		val text = motivationConfigurationSource.text

		val motivationQuoteText = text.split(split)

		return motivationQuoteText.map {
			val quoteAuthorSplit = it.split(splitAuthorText)
			val quote = quoteAuthorSplit[0]
			val author = quoteAuthorSplit.getOrElse(1) { MotivationQuotationDto.DEFAULT_MOTIVATION_AUTHOR }
			MotivationQuotationDto(quote, author)
		}.let(::MotivationsQuotationDto)
	}

	override fun sourceType(): MotivationSourceType = STATIC_TEXT
}