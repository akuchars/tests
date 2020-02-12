package akuchars.sender.domain

/**
 * Tworzy integracje do modułu sender
 */
interface SenderEventBus {

	fun sendNotifyEvent(event: NotificationEvent)
}