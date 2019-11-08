package akuchars.ui.email

import akuchars.application.email.command.EmailBodyResolver
import akuchars.application.email.command.EmailSender
import akuchars.application.email.command.EmailTemplateService
import akuchars.application.email.model.EmailTemplateDto
import akuchars.application.email.model.EmailTemplateResponseDto
import akuchars.application.email.model.EmailToSendDto
import org.springframework.http.HttpStatus.CREATED
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(("/api/v1/email"))
class EmailRestController(
		private val emailBodyResolver: EmailBodyResolver,
		private val emailSender: EmailSender,
		private val emailTemplateService: EmailTemplateService
) {

	@PostMapping("/send")
	fun sendEmail(@RequestBody emailToSendDto: EmailToSendDto) {
		val body = emailBodyResolver.prepareBodyForData(emailToSendDto)
		emailSender.sendEmail(emailToSendDto.to, body.title!!, body.value!!)
	}

	@PostMapping("/create")
	@ResponseStatus(value = CREATED)
	fun createNewEmailTemplate(@RequestBody emailTemplateDto: EmailTemplateDto): EmailTemplateResponseDto {
		//todo dopisać ControllerAdvice do błędów - FileAlreadyExist
		return emailTemplateService.saveNewTemplate(emailTemplateDto)
	}
}