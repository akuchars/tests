package akuchars.task.domain.model

import akuchars.common.domain.AbstractJpaEntity
import akuchars.common.kernel.ApplicationProperties
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(schema = ApplicationProperties.TASK_SCHEMA_NAME, name = "tags")
class Tag(val name: String) : AbstractJpaEntity()