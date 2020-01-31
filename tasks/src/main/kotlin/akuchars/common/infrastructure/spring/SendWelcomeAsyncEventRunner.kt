package akuchars.common.infrastructure.spring

import akuchars.common.domain.AsyncEvent
import akuchars.common.domain.EventBus
import akuchars.common.domain.EventType
import akuchars.common.kernel.ApplicationProperties
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class SendWelcomeAsyncEventRunner(private val eventBus: EventBus) : ApplicationRunner {

	override fun run(args: ApplicationArguments) {
		eventBus.sendAsync(ApplicationProperties.TASK_QUEUE_NAME, object : AsyncEvent {
			override fun getEventType(): EventType = EventType.SPRING
			override fun getEventMessage(): String = "After initialize spring boot"
		})
	}
}