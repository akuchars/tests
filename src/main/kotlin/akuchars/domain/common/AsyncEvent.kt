package akuchars.domain.common

import java.io.Serializable

interface AsyncEvent : Serializable {
	fun getEventMessage(): String
	fun getEventType(): EventType
}

enum class EventType {
	DELETED, CREATED, UPDATED, SPRING
}