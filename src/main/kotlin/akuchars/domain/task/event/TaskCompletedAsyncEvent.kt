package akuchars.domain.task.event

import akuchars.domain.common.AsyncEvent
import akuchars.domain.common.EventType
import akuchars.domain.common.EventType.CREATED
import akuchars.domain.common.EventType.UPDATED

class TaskCompletedAsyncEvent (private val taskId: Long): AsyncEvent {
	override fun getEventMessage(): String = "Task is completed $taskId"

	override fun getEventType(): EventType = UPDATED
}