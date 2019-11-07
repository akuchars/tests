package akuchars.application.task.command

import akuchars.domain.task.event.TaskAddedToProjectAsyncEvent
import akuchars.kernel.ApplicationProperties.MESSAGES_QUEUE_NAME
import akuchars.kernel.ApplicationProperties.TASK_QUEUE_NAME
import akuchars.kernel.logger
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