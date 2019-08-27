package akuchars.application.user.query

import akuchars.domain.user.model.User

interface UserQueryService {

	fun getLoggedUser(): User
}