package akuchars.motivation.application.model

class MotivationsQuotationDto private constructor(
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
		val author: String? = ""
)