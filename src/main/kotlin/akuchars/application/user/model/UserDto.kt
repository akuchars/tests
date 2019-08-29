package akuchars.application.user.model

class UserDto(
		val id: Long,
		val data: UserDataDto,
		val contactData: ContactDataDto,
		val roles: Set<RoleDto>
)

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
