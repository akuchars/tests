package akuchars.user.ui

import akuchars.user.application.model.UserDto
import akuchars.user.application.query.UserQueryService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(("/api/v1/user"))
class LoginRestController(private val userQueryService: UserQueryService) {

	@GetMapping("/me")
	@ResponseBody
	fun getLoggedUser(): UserDto = userQueryService.getLoggedUser()
}