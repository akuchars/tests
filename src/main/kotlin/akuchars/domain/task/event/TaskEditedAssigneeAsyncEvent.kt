package akuchars.domain.task.event

import akuchars.domain.common.AsyncEvent
import akuchars.domain.common.EventType
import akuchars.domain.common.EventType.UPDATED

class TaskEditedAssigneeAsyncEvent(val taskId: Long, val taskName: String) : AsyncEvent {
	override fun getEventMessage(): String = "Updated task: $taskId with name $taskName"

	override fun getEventType(): EventType = UPDATED
}