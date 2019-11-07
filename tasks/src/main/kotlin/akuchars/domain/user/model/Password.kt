package akuchars.domain.user.model

import javax.persistence.Embeddable

@Embeddable
data class Password (val value: String)
