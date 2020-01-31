package akuchars.task.domain.model

import javax.persistence.Embeddable

@Embeddable
data class TaskTitle(val value: String)