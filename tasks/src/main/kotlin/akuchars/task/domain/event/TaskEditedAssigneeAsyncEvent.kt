package akuchars.task.domain.event

import akuchars.common.domain.AsyncEvent
import akuchars.common.domain.EventType
import akuchars.common.domain.EventType.UPDATED

class TaskEditedAssigneeAsyncEvent(val taskId: Long, val taskName: String) : AsyncEvent {
	override fun getEventMessage(): String = "Updated task: $taskId with name $taskName"

	override fun getEventType(): EventType = UPDATED
}