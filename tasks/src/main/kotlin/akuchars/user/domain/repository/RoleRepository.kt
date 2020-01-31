package akuchars.user.domain.repository

import akuchars.user.domain.model.Role
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : CrudRepository<Role, Long> {
	fun getByRole(role: String): Role
}