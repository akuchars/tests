package akuchars.motivation.application.model

class MotivationsQuotationDto(
		val motivations: List<MotivationQuotationDto> = listOf()
) {

	companion object {
		fun create(vararg text: String) = MotivationsQuotationDto(text.map { MotivationQuotationDto(it) })

		fun empty() = MotivationsQuotationDto()

		fun copyFrom(motivations: List<MotivationQuotationDto>) = MotivationsQuotationDto(motivations)
	}
}

class MotivationQuotationDto(
		val text: String,
		val author: String? = DEFAULT_MOTIVATION_AUTHOR
) {
	companion object {
		const val DEFAULT_MOTIVATION_AUTHOR = ""
	}
}