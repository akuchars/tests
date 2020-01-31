package akuchars.task.domain.event

import akuchars.common.domain.AsyncEvent
import akuchars.common.domain.EventType
import akuchars.common.domain.EventType.UPDATED

class TaskCompletedAsyncEvent (private val taskId: Long): AsyncEvent {
	override fun getEventMessage(): String = "Task is completed $taskId"

	override fun getEventType(): EventType = UPDATED
}