package akuchars.ui.email

import akuchars.application.email.command.EmailBodyResolver
import akuchars.application.email.command.EmailSender
import akuchars.application.email.model.EmailToSendDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(("/api/v1/email"))
class EmailRestController(
		private val emailBodyResolver: EmailBodyResolver,
		private val emailSender: EmailSender
) {

	@PostMapping("/send")
	fun sendEmail(@RequestBody emailToSendDto: EmailToSendDto) {
		val body = emailBodyResolver.prepareBodyForData(emailToSendDto)
		emailSender.sendEmail(emailToSendDto.to, body.title!!, body.value!!)
	}
}