package akuchars.domain.user.model

import javax.persistence.Embeddable

@Embeddable
data class Email (val value: String)
