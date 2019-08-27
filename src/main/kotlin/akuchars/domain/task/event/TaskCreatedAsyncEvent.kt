package akuchars.domain.task.event

import akuchars.domain.common.AsyncEvent
import akuchars.domain.common.EventType

class TaskCreatedAsyncEvent(val taskId: Long, val title: String) : AsyncEvent {
	override fun getEventMessage(): String = "Task $title was created event"
	override fun getEventType(): EventType = EventType.CREATED
}