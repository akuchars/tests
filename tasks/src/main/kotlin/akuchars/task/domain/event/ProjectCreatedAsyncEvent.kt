package akuchars.task.domain.event

import akuchars.common.domain.AsyncEvent
import akuchars.common.domain.EventType
import akuchars.common.domain.EventType.CREATED

class ProjectCreatedAsyncEvent(val projectId: Long, val projectName: String) : AsyncEvent {
	override fun getEventMessage(): String = "Created new project $projectId and name: $projectName"
	override fun getEventType(): EventType = CREATED
}