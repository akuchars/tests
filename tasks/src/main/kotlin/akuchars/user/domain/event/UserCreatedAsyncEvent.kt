package akuchars.user.domain.event

import akuchars.common.domain.AsyncEvent
import akuchars.common.domain.EventType
import akuchars.common.domain.EventType.CREATED
import akuchars.user.domain.model.UserData

class UserCreatedAsyncEvent(val userId: Long, val userData: UserData) : AsyncEvent {
	override fun getEventMessage(): String =
			"Created new user: $userId with name ${userData.prettyString()}"

	override fun getEventType(): EventType = CREATED
}