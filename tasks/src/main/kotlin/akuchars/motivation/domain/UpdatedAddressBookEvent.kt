package akuchars.motivation.domain

import akuchars.common.domain.AsyncEvent
import akuchars.common.domain.EventType

class UpdatedAddressBookEvent(val addressBookId: Long, val to: String) : AsyncEvent {
	override fun getEventMessage(): String = "Updated address book $addressBookId and $to"

	override fun getEventType(): EventType = EventType.UPDATED
}