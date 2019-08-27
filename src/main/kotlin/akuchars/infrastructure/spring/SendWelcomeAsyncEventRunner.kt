package akuchars.infrastructure.spring

import akuchars.domain.common.AsyncEvent
import akuchars.domain.common.EventBus
import akuchars.domain.common.EventType
import akuchars.kernel.ApplicationProperties
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