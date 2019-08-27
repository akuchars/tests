package akuchars.domain.task.event

import akuchars.domain.common.AsyncEvent
import akuchars.domain.common.EventType
import akuchars.domain.common.EventType.UPDATED

class TaskChangedAssigneeAsyncEvent(
		val taskId: Long,
		val userId: Long
) : AsyncEvent {
	override fun getEventMessage(): String = "Task by id: $taskId has new assignee $userId event"
	override fun getEventType(): EventType = UPDATED
}