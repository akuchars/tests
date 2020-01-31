package akuchars.task.domain.event

import akuchars.common.domain.AsyncEvent
import akuchars.common.domain.EventType
import akuchars.common.domain.EventType.CREATED

class SubtaskAddedToTaskAsyncEvent : AsyncEvent {
	override fun getEventMessage(): String = "Create new checklist to task"

	override fun getEventType(): EventType = CREATED
}