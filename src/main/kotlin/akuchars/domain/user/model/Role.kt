package akuchars.domain.user.model

import akuchars.domain.common.AbstractJpaEntity
import akuchars.kernel.ApplicationProperties
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(schema = ApplicationProperties.USER_SCHEMA_NAME, name = "roles")
data class Role(@Column(name = "role") val role: String) : AbstractJpaEntity(), Serializable