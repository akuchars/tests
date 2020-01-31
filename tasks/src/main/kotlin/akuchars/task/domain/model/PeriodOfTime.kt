package akuchars.task.domain.model

import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Embeddable

@Embeddable
data class PeriodOfTime(
		val startDate: LocalDateTime,
		var endDate: LocalDateTime?
): Serializable {

	internal fun updateTime() {
		this.endDate = LocalDateTime.now()
	}

	companion object {

		@JvmStatic
		internal fun now(): PeriodOfTime {
			return PeriodOfTime(LocalDateTime.now(), null)
		}
	}
}
