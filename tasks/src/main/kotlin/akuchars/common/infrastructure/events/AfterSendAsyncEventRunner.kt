package akuchars.common.infrastructure.events

import akuchars.common.domain.AsyncEvent
import org.springframework.context.ApplicationEvent

interface AfterSendAsyncEventRunner {
	fun <E : AsyncEvent> runAfterEvent(event: E)
}

interface AfterSendSyncEventRunner {
	fun <E : ApplicationEvent> runAfterEvent(event: E)
}