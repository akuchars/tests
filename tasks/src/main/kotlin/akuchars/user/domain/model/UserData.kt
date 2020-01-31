package akuchars.user.domain.model

import java.io.Serializable
import javax.persistence.Embeddable

@Embeddable
data class UserData(
		val name: String,
		val surname: String
) : Serializable {
	fun prettyString(): String = "$name $surname"
}
