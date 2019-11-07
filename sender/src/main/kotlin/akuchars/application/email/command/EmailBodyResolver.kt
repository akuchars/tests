package akuchars.application.email.command

import akuchars.application.email.model.EmailBodyDto
import akuchars.application.email.model.EmailToSendDto
import akuchars.domain.email.repository.EmailTemplateLocationRepository
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Service
class EmailBodyResolver(
		private val templateEngine: TemplateEngine,
		private val emailTemplateLocationRepository: EmailTemplateLocationRepository
) {

	fun prepareBodyForData(emailToSendDto: EmailToSendDto): EmailBodyDto {
		val context = Context().apply {
			emailToSendDto.data.forEach {
				setVariable(it.key, it.value)
			}
		}
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
}