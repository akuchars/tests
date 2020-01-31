package akuchars.user.domain.model

import akuchars.common.domain.AbstractJpaEntity
import akuchars.common.kernel.ApplicationProperties
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(schema = ApplicationProperties.USER_SCHEMA_NAME, name = "roles")
data class Role(@Column(name = "role") val role: String) : AbstractJpaEntity(), Serializable