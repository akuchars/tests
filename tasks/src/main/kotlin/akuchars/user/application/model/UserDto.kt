package akuchars.user.application.model

open class FrontUserDto(
		val id: Long,
		val name: String,
		val surname: String,
		val email: String
)

class RegistrationFrontUserDto(
		id: Long,
		name: String,
		surname: String,
		email: String,
		val registrationInfo: RegistrationInfoTextDto
) : FrontUserDto(id, name, surname, email)

class UserDto(
		id: Long,
		data: UserDataDto,
		val contactData: ContactDataDto,
		val roles: Set<RoleDto>
) : FrontUserDto(id, data.name, data.surname, contactData.email)

class RoleDto(
		val roleKey: String
)

class ContactDataDto(
		val email: String,
		val phone: String?
)

class UserDataDto(
		val name: String,
		val surname: String
)
