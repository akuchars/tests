package akuchars.domain.task.model

import java.time.LocalDateTime
import javax.persistence.Embeddable

@Embeddable
data class ChangeEntityTime(
		val createdDate: LocalDateTime,
		private var updateDate: LocalDateTime?
) {

	internal fun updateTime() {
		this.updateDate = LocalDateTime.now()
	}

	companion object {

		@JvmStatic
		internal fun now(): ChangeEntityTime {
			return ChangeEntityTime(LocalDateTime.now(), null)
		}
	}
}
