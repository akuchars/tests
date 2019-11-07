package akuchars.application

import akuchars.domain.Scheduling
import java.time.LocalDateTime

interface SchedulingResolverApplicationService {
	fun isCorrectScheduled(scheduling: Scheduling) : Boolean
	fun recalculateSchedule(date: LocalDateTime, scheduling: Scheduling): LocalDateTime
}