package akuchars.notification.email.application.model

data class EmailToSendDto(
		val to: String,
		val mailCode: String,
		val titleData: Map<String, *>,
		val data: Map<String, *>
)