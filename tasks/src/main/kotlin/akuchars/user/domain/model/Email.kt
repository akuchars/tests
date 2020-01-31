package akuchars.user.domain.model

import javax.persistence.Embeddable

@Embeddable
data class Email (val value: String)
