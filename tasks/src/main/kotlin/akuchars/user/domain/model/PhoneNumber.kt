package akuchars.user.domain.model

import javax.persistence.Embeddable

@Embeddable
data class PhoneNumber (val value: String)
