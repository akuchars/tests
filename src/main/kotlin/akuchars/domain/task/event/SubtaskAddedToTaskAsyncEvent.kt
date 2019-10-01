package akuchars.domain.task.event

import akuchars.domain.common.AsyncEvent
import akuchars.domain.common.EventType
import akuchars.domain.common.EventType.CREATED

class SubtaskAddedToTaskAsyncEvent : AsyncEvent {
	override fun getEventMessage(): String = "Create new checklist to task"

	override fun getEventType(): EventType = CREATED
}