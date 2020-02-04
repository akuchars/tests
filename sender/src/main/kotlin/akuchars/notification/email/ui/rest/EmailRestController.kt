package akuchars.notification.email.ui.rest

import akuchars.notification.common.application.NotificationService
import akuchars.notification.common.application.model.NotificationDataDto
import akuchars.notification.common.application.model.NotificationType.EMAIL
import akuchars.notification.email.application.command.EmailBodyResolver
import akuchars.notification.email.application.command.EmailTemplateService
import akuchars.notification.email.application.model.EmailTemplateDto
import akuchars.notification.email.application.model.EmailTemplateResponseDto
import akuchars.notification.email.application.model.EmailToSendDto
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(("/api/v1/email"))
class EmailRestController internal constructor(
		private val emailBodyResolver: EmailBodyResolver,
		private val emailSender: NotificationService,
		private val emailTemplateService: EmailTemplateService
) {

	@PostMapping("/send")
	// todo akuchars - to powinien być rest-dto
	fun sendEmail(@RequestBody emailToSendDto: EmailToSendDto) {
		val body = emailBodyResolver.prepareBodyForData(emailToSendDto)
		val notificationData = NotificationDataDto(emailToSendDto.to, body.title!!, body.value!!, EMAIL)
		emailSender.notify(notificationData)
	}

	@PutMapping("/create")
	@ResponseStatus(value = CREATED)
	// todo akuchars - to powinien być rest-dto'sy
	fun createNewEmailTemplate(@RequestBody emailTemplateDto: EmailTemplateDto): EmailTemplateResponseDto {
		//todo dopisać ControllerAdvice do błędów - FileAlreadyExist
		return emailTemplateService.saveNewTemplate(emailTemplateDto)
	}
}