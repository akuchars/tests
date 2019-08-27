package akuchars.infrastructure.events

import akuchars.domain.common.AsyncEvent
import akuchars.domain.common.EventBus
import akuchars.kernel.ProfileProperties.TEST_PROFILE
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEvent
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(TEST_PROFILE)
class MockedEventBus : EventBus() {

	companion object {
		val logger: Logger = LoggerFactory.getLogger(EventBus::class.java)
	}

	override fun <E : ApplicationEvent> sendSyncInner(event: E) {
		logger.info("Invoke sendSyncInner method with params: $event")
	}

	override fun <E : ApplicationEvent> afterSendSyncInner(event: E) {
		logger.info("Invoke afterSendSyncInner method with params: $event")
	}

	override fun <E : AsyncEvent> sendAsyncInner(queueName: String, event: E) {
		logger.info("Invoke sendAsyncInner method with params: $event")
	}

	override fun <E : AsyncEvent> afterSendAsyncInner(event: E) {
		logger.info("Invoke afterSendAsyncInner method with params: $event")
	}
}