package akuchars.infrastructure.spring

import akuchars.domain.user.model.User

object SecurityContextUserHolder {
	lateinit var loggedUser: User
}