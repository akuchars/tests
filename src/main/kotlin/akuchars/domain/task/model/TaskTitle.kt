package akuchars.domain.task.model

import javax.persistence.Embeddable

@Embeddable
data class TaskTitle(val value: String)