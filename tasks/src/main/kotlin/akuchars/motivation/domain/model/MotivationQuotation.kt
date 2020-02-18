package akuchars.motivation.domain.model

import akuchars.common.domain.AbstractJpaEntity
import akuchars.common.kernel.ApplicationProperties
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(schema = ApplicationProperties.MOTIVATION_SCHEMA_NAME, name = "quatations")
data class MotivationQuotation(
		var text: String,
		var author: String?
) : AbstractJpaEntity() {

	fun modify(text: String, author: String): MotivationQuotation {
		return apply {
			this.text = text
			this.author = author
		}
	}

}