package akuchars.task.domain.model

import javax.persistence.Embeddable

@Embeddable
data class TaskMainGoal(val value: String)