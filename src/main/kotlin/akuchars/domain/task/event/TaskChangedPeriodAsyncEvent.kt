package akuchars.domain.task.event

import akuchars.domain.common.AsyncEvent
import akuchars.domain.common.EventType
import akuchars.domain.task.model.PeriodOfTime

class TaskChangedPeriodAsyncEvent(val period: PeriodOfTime) : AsyncEvent {
	override fun getEventMessage(): String = "Task has new period $period event"
	override fun getEventType(): EventType = EventType.UPDATED
}