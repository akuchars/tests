package akuchars.motivation.domain.model

import akuchars.motivation.domain.model.MotivationSourceType.STATIC_TEXT


//todo akuchars dodawania konfiguracji poprzez resta per użytkownik - zapisywanie również w bazie
abstract class MotivationConfigurationSource {
	abstract fun getType(): MotivationSourceType
}

class StaticMotivationConfigurationSource : MotivationConfigurationSource() {
	override fun getType(): MotivationSourceType = MotivationSourceType.STATIC
}

class StaticTextMotivationConfigurationSource(
		val text: String,
		val splitRegex: String = ";",
		val splitAuthorText: String = "~"
) : MotivationConfigurationSource() {
	override fun getType(): MotivationSourceType = STATIC_TEXT
}