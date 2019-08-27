package akuchars.domain.common

import org.springframework.context.ApplicationEvent

abstract class EventBus {

	fun <E: ApplicationEvent> sendSync(event: E) {
		sendSyncInner(event)
		afterSendSyncInner(event)
	}

	fun <E: AsyncEvent> sendAsync(queueName: String, event: E) {
		sendAsyncInner(queueName, event)
		afterSendAsyncInner(event)
	}

	protected abstract fun <E: ApplicationEvent> sendSyncInner(event: E)
	protected abstract fun <E: ApplicationEvent> afterSendSyncInner(event: E)
	protected abstract fun <E: AsyncEvent> sendAsyncInner(queueName: String, event: E)
	protected abstract fun <E: AsyncEvent> afterSendAsyncInner(event: E)
}