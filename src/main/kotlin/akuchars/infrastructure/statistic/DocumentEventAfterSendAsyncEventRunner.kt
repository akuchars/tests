package akuchars.infrastructure.statistic

import akuchars.domain.common.AsyncEvent
import akuchars.domain.statistic.model.DocumentedEvent
import akuchars.domain.statistic.repository.DocumentedEventRepository
import akuchars.infrastructure.events.AfterSendAsyncEventRunner
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