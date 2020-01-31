package akuchars.task.domain.event

import akuchars.common.domain.AsyncEvent
import akuchars.common.domain.EventType
import akuchars.common.domain.EventType.UPDATED

class SubtaskCompletedInTaskAsyncEvent (private val subtaskId: Long, private val taskId: Long): AsyncEvent {
	override fun getEventMessage(): String = "Subtask: $subtaskId completed in task: $taskId"

	override fun getEventType(): EventType = UPDATED
}