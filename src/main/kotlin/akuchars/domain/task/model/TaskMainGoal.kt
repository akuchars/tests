package akuchars.domain.task.model

import javax.persistence.Embeddable

@Embeddable
data class TaskMainGoal(val value: String)