package akuchars.user.infrastructure

import akuchars.user.domain.model.Email
import akuchars.user.domain.repository.UserRepository
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