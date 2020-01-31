package akuchars.task.domain.event

import akuchars.common.domain.AsyncEvent
import akuchars.common.domain.EventType
import akuchars.common.domain.EventType.UPDATED

class TagAddedToTaskAsyncEvent (val tagName: String, val taskName: String): AsyncEvent {
	override fun getEventMessage(): String  = "Tag: $tagName added to task by name: $taskName"

	override fun getEventType(): EventType = UPDATED
}