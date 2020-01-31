package akuchars.common.infrastructure.spring

import akuchars.common.kernel.ApplicationProperties.EXCHANGE_NAME
import akuchars.common.kernel.ApplicationProperties.MESSAGES_QUEUE_NAME
import akuchars.common.kernel.ApplicationProperties.PERSONAL_QUEUE_NAME
import akuchars.common.kernel.ApplicationProperties.TASK_QUEUE_NAME
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Exchange
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitConfiguration {

	//todo move queue names to application.properties file

	@Bean
	fun taskQueue() = Queue(TASK_QUEUE_NAME, false)

	@Bean
	fun personalQueue() = Queue(PERSONAL_QUEUE_NAME, false)

	@Bean
	fun messagesQueue() = Queue(MESSAGES_QUEUE_NAME, false)

	@Bean
	fun fanoutExchange(): Exchange = FanoutExchange(EXCHANGE_NAME)

	@Bean
	fun taskBinding(fanoutExchange: Exchange, taskQueue: Queue) =
			BindingBuilder.bind(taskQueue).to(fanoutExchange).with("*").noargs()

	@Bean
	fun personalBinding(fanoutExchange: Exchange, personalQueue: Queue) =
			BindingBuilder.bind(personalQueue).to(fanoutExchange).with("*").noargs()

	@Bean
	fun messagesBinding(fanoutExchange: Exchange, messagesQueue: Queue) =
			BindingBuilder.bind(messagesQueue).to(fanoutExchange).with("*").noargs()
}