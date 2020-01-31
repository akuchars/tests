package akuchars.user.domain.model

import akuchars.common.domain.AbstractJpaEntity
import akuchars.common.domain.EventBus
import akuchars.common.kernel.ApplicationProperties
import akuchars.common.kernel.ApplicationProperties.PERSONAL_QUEUE_NAME
import akuchars.common.kernel.ApplicationProperties.USER_SCHEMA_NAME
import akuchars.user.domain.event.UserCreatedAsyncEvent
import akuchars.user.domain.repository.UserRepository
import javax.persistence.AttributeOverride
import javax.persistence.AttributeOverrides
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.FetchType.EAGER
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(schema = ApplicationProperties.USER_SCHEMA_NAME, name = "users")
data class User(
		@Embedded
		@AttributeOverrides(
				AttributeOverride(name = "name", column = Column(name = "name")),
				AttributeOverride(name = "surname", column = Column(name = "surname"))
		)
		val userData: UserData,

		@Embedded
		@AttributeOverrides(AttributeOverride(name = "value", column = Column(name = "email")))
		val email: Email,

		@Embedded
		@AttributeOverrides(AttributeOverride(name = "value", column = Column(name = "phone")))
		var phoneNumber: PhoneNumber? = null,

		@Embedded
		@AttributeOverrides(AttributeOverride(name = "value", column = Column(name = "password")))
		val password: Password,

		@ManyToMany(fetch = EAGER)
		@JoinTable(
				schema = USER_SCHEMA_NAME,
				name = "users_roles",
				joinColumns = [JoinColumn(name = "user_id")],
				inverseJoinColumns = [JoinColumn(name = "role_id")]
		)
		val roles: Set<Role>,

		val enable: Boolean
) : AbstractJpaEntity() {

	companion object {

		fun createUser(eventBus: EventBus, userRepository: UserRepository,
		               name: UserData, email: Email,
		               password: Password, roles: Set<Role>,
		               phoneNumber: PhoneNumber? = null
		): User {
			return User(name, email, phoneNumber, password, roles, true).apply {
				userRepository.save(this)
			}.also {
				eventBus.sendAsync(PERSONAL_QUEUE_NAME, UserCreatedAsyncEvent(it.id, it.userData))
			}
		}
	}
}
