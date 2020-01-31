package akuchars.task.domain.event

import akuchars.common.domain.AsyncEvent
import akuchars.common.domain.EventType
import akuchars.task.domain.model.PeriodOfTime

class TaskChangedPeriodAsyncEvent(val period: PeriodOfTime) : AsyncEvent {
	override fun getEventMessage(): String = "Task has new period $period event"
	override fun getEventType(): EventType = EventType.UPDATED
}