package akuchars.infrastructure

import akuchars.application.SchedulingResolverApplicationService
import akuchars.domain.Scheduling
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
internal class SchedulingResolverApplicationServiceImpl : SchedulingResolverApplicationService {
	override fun isCorrectScheduled(scheduling: Scheduling): Boolean {
		//todo tutaj magia ma się zadziać
		return true
	}

	override fun recalculateSchedule(date: LocalDateTime, scheduling: Scheduling): LocalDateTime {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}
}