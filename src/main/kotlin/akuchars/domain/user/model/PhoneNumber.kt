package akuchars.domain.user.model

import javax.persistence.Embeddable

@Embeddable
data class PhoneNumber (val value: String)
