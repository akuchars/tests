package akuchars.user.domain.model

import javax.persistence.Embeddable

@Embeddable
data class Password (val value: String)
