package akuchars.user.domain.repository

import akuchars.user.domain.model.Email
import akuchars.user.domain.model.User
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : PagingAndSortingRepository<User, Long> {
	fun findByEmail(email: Email): User?
}