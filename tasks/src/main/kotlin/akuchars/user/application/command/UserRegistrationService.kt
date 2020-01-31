package akuchars.user.application.command

import akuchars.common.application.command.FrontDtoConverter
import akuchars.common.application.model.FrontDto
import akuchars.common.domain.EventBus
import akuchars.user.application.model.RegistrationFrontUserDto
import akuchars.user.application.model.RegistrationInfoTextDto
import akuchars.user.application.model.UserForm
import akuchars.user.domain.model.Email
import akuchars.user.domain.model.Password
import akuchars.user.domain.model.PhoneNumber
import akuchars.user.domain.model.RoleType.USER
import akuchars.user.domain.model.User
import akuchars.user.domain.model.UserData
import akuchars.user.domain.repository.RoleRepository
import akuchars.user.domain.repository.UserRepository
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
