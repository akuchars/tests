package akuchars.statistic.infrastructure


import akuchars.common.domain.AsyncEvent
import akuchars.common.infrastructure.events.AfterSendAsyncEventRunner
import akuchars.statistic.domain.model.DocumentedEvent
import akuchars.statistic.domain.repository.DocumentedEventRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class DocumentEventAfterSendAsyncEventRunner(
		private val documentedEventRepository: DocumentedEventRepository
) : AfterSendAsyncEventRunner {

	override fun <E : AsyncEvent> runAfterEvent(event: E) {
		documentedEventRepository.insert(
				DocumentedEvent(
						null,
						event.getEventMessage(),
						event.getEventType().name,
						event::class.java.name,
						LocalDateTime.now(),
						event
				)
		)
	}
}