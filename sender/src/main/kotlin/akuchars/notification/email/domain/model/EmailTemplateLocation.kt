package akuchars.notification.email.domain.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class EmailTemplateLocation(
		@Id val key: String,
		val location: String,
		val title: String
)