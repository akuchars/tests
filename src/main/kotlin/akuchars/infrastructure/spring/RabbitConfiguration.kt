package akuchars.infrastructure.spring

import akuchars.kernel.ApplicationProperties.PERSONAL_QUEUE_NAME
import akuchars.kernel.ApplicationProperties.TASK_QUEUE_NAME
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfiguration {

	@Bean
	fun taskQueue() = Queue(TASK_QUEUE_NAME, false)

	@Bean
	fun personalQueue() = Queue(PERSONAL_QUEUE_NAME, false)
}