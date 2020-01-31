package akuchars.user.application.model

class UserForm(
		var name: String,
		var surname: String,
		var email: String,
		var password: String,
		var phoneNumber : String? = null
)