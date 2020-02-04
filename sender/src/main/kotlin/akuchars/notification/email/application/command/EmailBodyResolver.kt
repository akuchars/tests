package akuchars.notification.email.application.command

import akuchars.notification.email.application.model.EmailBodyDto
import akuchars.notification.email.application.model.EmailToSendDto
import akuchars.notification.email.domain.repository.EmailTemplateLocationRepository
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Service
internal class EmailBodyResolver(
		private val templateEngine: TemplateEngine,
		private val emailTemplateLocationRepository: EmailTemplateLocationRepository
) {

	fun prepareBodyForData(emailToSendDto: EmailToSendDto): EmailBodyDto {
		val context = createEmailContext(emailToSendDto)
		val emailTemplateLocation = emailTemplateLocationRepository.findByKey(emailToSendDto.mailCode)
		emailToSendDto.data.forEach {
			emailTemplateLocation?.title?.replace(it.key, it.value.toString(), true)
		}
		val templateName = emailTemplateLocation?.location
		return EmailBodyDto(
				emailTemplateLocation?.title,
				templateEngine.process(templateName, context)
		)
	}

	private fun createEmailContext(emailToSendDto: EmailToSendDto): Context {
		return Context().apply {
			emailToSendDto.data.forEach {
				this.setVariable(it.key, it.value)
			}
		}
	}
}