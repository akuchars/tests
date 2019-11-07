package akuchars.domain.task.model

import javax.persistence.Embeddable

@Embeddable
data class TaskContent(val value: String)
