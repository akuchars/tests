package akuchars.task.domain.event

import akuchars.common.domain.AsyncEvent
import akuchars.common.domain.EventType
import akuchars.common.domain.EventType.UPDATED

class TaskChangedAssigneeAsyncEvent(
		val taskId: Long,
		val userId: Long
) : AsyncEvent {
	override fun getEventMessage(): String = "Task by id: $taskId has new assignee $userId event"
	override fun getEventType(): EventType = UPDATED
}