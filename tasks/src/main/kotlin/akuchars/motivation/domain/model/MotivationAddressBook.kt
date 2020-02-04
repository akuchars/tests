package akuchars.motivation.domain.model

class MotivationAddressBook(
		val to: String,
		val type: NotificationType,
		val configurationSources: List<MotivationConfigurationSource>
)