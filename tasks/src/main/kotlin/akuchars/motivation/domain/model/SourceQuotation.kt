package akuchars.motivation.domain.model

import akuchars.common.domain.AbstractJpaEntity
import akuchars.common.kernel.ApplicationProperties
import javax.persistence.Entity
import javax.persistence.EnumType.STRING
import javax.persistence.Enumerated
import javax.persistence.FetchType.LAZY
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(schema = ApplicationProperties.MOTIVATION_SCHEMA_NAME, name = "books_quatations")
class SourceQuotation(
		@ManyToOne
		@JoinColumn(name = "quatations_id")
		var motivation: Quotation,
		@ManyToOne(fetch = LAZY)
		@JoinColumn(name = "address_book_id")
		private val addressBook: AddressBook,
		var active: Boolean,
		@Enumerated(STRING)
		var type: SourceType
) : AbstractJpaEntity() {
	fun modify(motivationText: String, author: String, active: Boolean): SourceQuotation {
		return apply {
			this.active = active
			this.motivation.text = motivationText
			this.motivation.author = author
		}
	}
}