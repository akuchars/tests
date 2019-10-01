package akuchars.domain.task.event

import akuchars.domain.common.AsyncEvent
import akuchars.domain.common.EventType
import akuchars.domain.common.EventType.CREATED
import akuchars.domain.common.EventType.UPDATED

class SubtaskCompletedInTaskAsyncEvent (private val subtaskId: Long, private val taskId: Long): AsyncEvent {
	override fun getEventMessage(): String = "Subtask: $subtaskId completed in task: $taskId"

	override fun getEventType(): EventType = UPDATED
}