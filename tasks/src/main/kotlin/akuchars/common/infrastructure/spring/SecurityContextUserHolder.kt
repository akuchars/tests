package akuchars.common.infrastructure.spring

import akuchars.user.domain.model.User

object SecurityContextUserHolder {
	lateinit var loggedUser: User
}