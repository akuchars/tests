package akuchars.ui.rest.personal

import akuchars.application.common.model.FrontDto
import akuchars.application.user.command.UserRegistrationService
import akuchars.application.user.model.FrontUserDto
import akuchars.application.user.model.RegistrationFrontUserDto
import akuchars.application.user.model.UserForm
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(("/api/v1/user"))
class RegistrationRestController(private val userRegistrationService: UserRegistrationService) {

	@PutMapping("/registrations")
	@ResponseBody
	@ResponseStatus(CREATED)
	fun registryNewUser(@RequestBody userForm: UserForm): ResponseEntity<FrontDto<RegistrationFrontUserDto>> =
			userRegistrationService.createNewUser(userForm).toResponseEntity()
}