package akuchars.common.infrastructure.events

import akuchars.common.domain.AsyncEvent
import akuchars.common.domain.EventBus
import akuchars.common.kernel.ApplicationProperties.EXCHANGE_NAME
import akuchars.common.kernel.ProfileProperties.PROD_PROFILE
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(PROD_PROFILE)
class EventBusImpl(
		private val applicationPublisher: ApplicationEventPublisher,
		private val rabbitTemplate: RabbitTemplate,
		@Autowired(required = false) private val afterSendSyncRunner: List<AfterSendSyncEventRunner> = listOf(),
		@Autowired(required = false) private val afterSendAsyncRunner: List<AfterSendAsyncEventRunner> = listOf()
) : EventBus() {

	override fun <E : ApplicationEvent> sendSyncInner(event: E) {
		applicationPublisher.publishEvent(event)
	}

	override fun <E : ApplicationEvent> afterSendSyncInner(event: E) {
		GlobalScope.launch {
			afterSendSyncRunner.forEach {
				kotlin.runCatching {
					it.runAfterEvent(event)
				}
			}
		}
	}

	override fun <E : AsyncEvent> sendAsyncInner(queueName: String, event: E) {
		rabbitTemplate.convertAndSend(EXCHANGE_NAME, "*", event)
	}

	override fun <E : AsyncEvent> afterSendAsyncInner(event: E) {
		GlobalScope.launch {
			afterSendAsyncRunner.forEach {
				kotlin.runCatching {
					it.runAfterEvent(event)
				}
			}
		}
	}
}

