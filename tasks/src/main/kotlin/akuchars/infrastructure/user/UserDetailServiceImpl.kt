package akuchars.infrastructure.user

import akuchars.domain.user.model.Email
import akuchars.domain.user.repository.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailServiceImpl(
		private val userRepository: UserRepository
) : UserDetailsService {

	override fun loadUserByUsername(email: String): UserDetails {
		return userRepository.findByEmail(Email(email))!!.let {
			User(it.email.value, it.password.value, it.roles.map { GrantedAuthority { it.role } })
		}
	}
}