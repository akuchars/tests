package akuchars.domain.task.model

import akuchars.domain.common.AbstractJpaEntity
import akuchars.kernel.ApplicationProperties
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(schema = ApplicationProperties.TASK_SCHEMA_NAME, name = "tags")
class Tag(val name: String) : AbstractJpaEntity()