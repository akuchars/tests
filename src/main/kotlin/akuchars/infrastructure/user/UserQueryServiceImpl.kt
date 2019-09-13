package akuchars.infrastructure.user

import akuchars.application.user.model.UserDto
import akuchars.application.user.query.UserQueryService
import akuchars.application.user.query.toDto
import akuchars.domain.user.model.Email
import akuchars.domain.user.repository.UserRepository
import akuchars.kernel.ProfileProperties
import org.springframework.context.annotation.Profile
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
@Profile(ProfileProperties.TEST_PROFILE)
class TestUserQueryService(
		private val userRepository: UserRepository
) : UserQueryService {
	override fun getUserById(id: Long): UserDto = userRepository.findByIdOrNull(id)!!.toDto()

	override fun getLoggedUser(): UserDto {
		val userPaginateResult = userRepository.findAll(PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id")));
		return userPaginateResult.get().findFirst().get().toDto()
	}
}

@Service
class ProdUserQueryService(
		private val userRepository: UserRepository
) : UserQueryService {
	override fun getUserById(id: Long): UserDto = userRepository.findByIdOrNull(id)!!.toDto()

	override fun getLoggedUser(): UserDto {
		return when (val principal = SecurityContextHolder.getContext().authentication.principal) {
			is UserDetails -> userRepository.findByEmail(Email(principal.username))!!.toDto()
			else -> throw IllegalStateException("No user is logged")
		}
	}
}