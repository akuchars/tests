package akuchars.user.infrastructure

import akuchars.common.kernel.ProfileProperties
import akuchars.user.application.model.UserDto
import akuchars.user.application.query.UserQueryService
import akuchars.user.application.query.toDto
import akuchars.user.domain.model.Email
import akuchars.user.domain.repository.UserRepository
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