package akuchars.domain

import akuchars.application.SchedulingResolverApplicationService
import akuchars.infrastructure.SchedulingResolverApplicationServiceImpl
import javax.persistence.Embeddable

@Embeddable
class Scheduling private constructor(private val value: String) {

	companion object {
		fun createNew(value: String): Scheduling {
			return createNewInner(value, SchedulingResolverApplicationServiceImpl())
		}

		private fun createNewInner(value: String, resolver: SchedulingResolverApplicationService): Scheduling {
			val scheduling = Scheduling(value)
			check(resolver.isCorrectScheduled(scheduling)) { "Cannot create scheduling for params: $value" }
			return scheduling
		}
	}
}