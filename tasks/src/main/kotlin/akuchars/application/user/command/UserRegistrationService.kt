package akuchars.application.user.command

import akuchars.application.common.command.FrontDtoConverter
import akuchars.application.common.model.FrontDto
import akuchars.application.user.model.RegistrationFrontUserDto
import akuchars.application.user.model.RegistrationInfoTextDto
import akuchars.application.user.model.UserForm
import akuchars.domain.common.EventBus
import akuchars.domain.user.model.Email
import akuchars.domain.user.model.Password
import akuchars.domain.user.model.PhoneNumber
import akuchars.domain.user.model.RoleType.USER
import akuchars.domain.user.model.User
import akuchars.domain.user.model.UserData
import akuchars.domain.user.repository.RoleRepository
import akuchars.domain.user.repository.UserRepository
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserRegistrationService(
		private val eventBus: EventBus,
		private val passwordEncoder: PasswordEncoder,
		private val userRepository: UserRepository,
		private val roleRepository: RoleRepository,
		private val messageSource: MessageSource,
		private val frontDtoConverter: FrontDtoConverter
) {

	fun createNewUser(userForm: UserForm): FrontDto<RegistrationFrontUserDto> {
		return frontDtoConverter.toFrontDto {
			createNewUserInner(userForm)
		}
	}

	@Transactional
	private fun createNewUserInner(userForm: UserForm): RegistrationFrontUserDto {
		return User.createUser(
				eventBus, userRepository,
				UserData(userForm.name, userForm.surname),
				Email(userForm.email),
				Password(passwordEncoder.encode(userForm.password)),
				setOf(roleRepository.getByRole(USER.name)),
				userForm.phoneNumber?.let(::PhoneNumber)
		).let {
			RegistrationFrontUserDto(
					it.id,
					it.userData.name,
					it.userData.surname,
					it.email.value,
					RegistrationInfoTextDto(
							messageSource.getMessage("user.welcome.msg", arrayOf(it.userData.name), LocaleContextHolder.getLocale())
					)
			)
		}
	}
}
