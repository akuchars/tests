package akuchars.domain.user.event

import akuchars.domain.common.AsyncEvent
import akuchars.domain.common.EventType
import akuchars.domain.common.EventType.CREATED
import akuchars.domain.user.model.Role
import akuchars.domain.user.model.UserData

class UserCreatedAsyncEvent(val userId: Long, val userData: UserData, val roles: Set<Role>) : AsyncEvent {
	override fun getEventMessage(): String =
			"Created new user: $userId with name ${userData.prettyString()} with roles: ${roles.joinToString(",")}"

	override fun getEventType(): EventType = CREATED
}