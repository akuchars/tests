package akuchars.domain.user.repository

import akuchars.domain.user.model.Email
import akuchars.domain.user.model.User
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : PagingAndSortingRepository<User, Long> {
	fun findByEmail(email: Email): User?
}