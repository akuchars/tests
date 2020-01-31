package akuchars.task.domain.event

import akuchars.common.domain.AsyncEvent
import akuchars.common.domain.EventType

class TaskAddedToProjectAsyncEvent(val taskId: Long, val projectId: Long) : AsyncEvent {
	override fun getEventMessage(): String = "Task $taskId added to project $projectId event"
	override fun getEventType(): EventType = EventType.UPDATED
}