package akuchars.infrastructure.user

import akuchars.application.user.query.UserQueryService
import akuchars.domain.user.model.User
import akuchars.domain.user.repository.UserRepository
import akuchars.kernel.ProfileProperties
import org.springframework.context.annotation.Profile
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
@Profile(ProfileProperties.TEST_PROFILE, ProfileProperties.PROD_PROFILE)
//TODO create new UserQueryService for production
class TestUserQueryService (
		private val userRepository: UserRepository
): UserQueryService {

	override fun getLoggedUser(): User {
		val user = userRepository.findAll(PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id")));
		return user.get().findFirst().get()
	}
}