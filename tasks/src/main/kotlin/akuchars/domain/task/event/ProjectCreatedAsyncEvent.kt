package akuchars.domain.task.event

import akuchars.domain.common.AsyncEvent
import akuchars.domain.common.EventType
import akuchars.domain.common.EventType.CREATED

class ProjectCreatedAsyncEvent(val projectId: Long, val projectName: String) : AsyncEvent {
	override fun getEventMessage(): String = "Created new project $projectId and name: $projectName"
	override fun getEventType(): EventType = CREATED
}