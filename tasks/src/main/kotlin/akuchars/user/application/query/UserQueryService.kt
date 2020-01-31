package akuchars.user.application.query

import akuchars.user.application.model.ContactDataDto
import akuchars.user.application.model.RoleDto
import akuchars.user.application.model.UserDataDto
import akuchars.user.application.model.UserDto
import akuchars.user.domain.model.User

interface UserQueryService {

	fun getLoggedUser(): UserDto
	fun getUserById(id: Long): UserDto

}

fun User.toDto(): UserDto {
	return UserDto(
			id,
			UserDataDto(userData.name, userData.surname),
			ContactDataDto(email.value, phoneNumber?.value),
			roles.map { RoleDto(it.role) }.toSet()
	)
}