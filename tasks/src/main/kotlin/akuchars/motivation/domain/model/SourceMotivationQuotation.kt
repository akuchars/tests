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
class SourceMotivationQuotation(
		@ManyToOne
		@JoinColumn(name = "quatations_id")
		val motivation: MotivationQuotation,
		@ManyToOne(fetch = LAZY)
		@JoinColumn(name = "address_book_id")
		private val addressBook: MotivationAddressBook,
		val active: Boolean,
		@Enumerated(STRING)
		val type: MotivationSourceType
) : AbstractJpaEntity()