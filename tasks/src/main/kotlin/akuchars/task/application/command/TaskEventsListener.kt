package akuchars.task.application.command

import akuchars.common.kernel.ApplicationProperties.MESSAGES_QUEUE_NAME
import akuchars.common.kernel.ApplicationProperties.TASK_QUEUE_NAME
import akuchars.common.kernel.logger
import akuchars.task.domain.event.TaskAddedToProjectAsyncEvent
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class TaskEventsListener {

	@RabbitListener(queues = [MESSAGES_QUEUE_NAME])
	fun handleTaskIsCreatedEventFromMessageQueue(event: TaskAddedToProjectAsyncEvent) {
		logger.info("Queue: $MESSAGES_QUEUE_NAME: In task added to project listener")
	}

	@RabbitListener(queues = [TASK_QUEUE_NAME])
	fun handleTaskIsCreatedEventFromTaskQueue(event: TaskAddedToProjectAsyncEvent) {
		logger.info("Queue: $TASK_QUEUE_NAME: In task added to project listener")
	}
}