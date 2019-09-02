package akuchars.application.user.query

import akuchars.application.user.model.ContactDataDto
import akuchars.application.user.model.RoleDto
import akuchars.application.user.model.UserDataDto
import akuchars.application.user.model.UserDto
import akuchars.domain.user.model.User

interface UserQueryService {

	fun getLoggedUser(): UserDto
	fun getUserById(id: Long): UserDto

	fun User.toDto(): UserDto {
		return UserDto(
				id,
				UserDataDto(userData.name, userData.surname),
				ContactDataDto(email.value, phoneNumber?.value),
				roles.map { RoleDto(it.role) }.toSet()
		)
	}
}