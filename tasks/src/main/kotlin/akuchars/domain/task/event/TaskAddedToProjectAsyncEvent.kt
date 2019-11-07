package akuchars.domain.task.event

import akuchars.domain.common.AsyncEvent
import akuchars.domain.common.EventType

class TaskAddedToProjectAsyncEvent(val taskId: Long, val projectId: Long) : AsyncEvent {
	override fun getEventMessage(): String = "Task $taskId added to project $projectId event"
	override fun getEventType(): EventType = EventType.UPDATED
}