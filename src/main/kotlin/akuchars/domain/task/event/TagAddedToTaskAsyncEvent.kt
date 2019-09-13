package akuchars.domain.task.event

import akuchars.domain.common.AsyncEvent
import akuchars.domain.common.EventType
import akuchars.domain.common.EventType.UPDATED

class TagAddedToTaskAsyncEvent (val tagName: String, val taskName: String): AsyncEvent {
	override fun getEventMessage(): String  = "Tag: $tagName added to task by name: $taskName"

	override fun getEventType(): EventType = UPDATED
}