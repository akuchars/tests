package akuchars.task.domain.model

import javax.persistence.Embeddable

@Embeddable
data class TaskContent(val value: String)
