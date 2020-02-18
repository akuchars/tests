package akuchars.motivation.application.model

class MotivationDataDto(
		val id: Long,
		val text: String,
		val author: String,
		val active: Boolean,
		val typeTextMsg: String
)