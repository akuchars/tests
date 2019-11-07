package akuchars.domain.task.model

import akuchars.domain.common.AbstractJpaEntity
import akuchars.domain.task.model.TaskStatus.DONE
import akuchars.kernel.ApplicationProperties
import java.util.Objects
import javax.persistence.AttributeOverride
import javax.persistence.AttributeOverrides
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(schema = ApplicationProperties.TASK_SCHEMA_NAME, name = "subtasks")
class Subtask(
		@Embedded
		@AttributeOverrides(AttributeOverride(name = "value", column = Column(name = "title")))
		var taskTitle: TaskTitle,

		@Enumerated(EnumType.STRING)
		var status: TaskStatus,

		@ManyToOne
		@JoinColumn(name = "parent_id")
		var parent: Task
) : AbstractJpaEntity() {
	fun isDone() = status == DONE

	fun markAsDone(): Subtask {
		this.status = DONE
		return this
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false
		if (!super.equals(other)) return false

		other as Subtask

		if (taskTitle != other.taskTitle) return false
		if (status != other.status) return false
		if (parent.id != other.parent.id) return false

		return true
	}

	override fun hashCode(): Int {
		var result = super.hashCode()
		result = 31 * result + taskTitle.hashCode()
		result = 31 * result + status.hashCode()
		result = 31 * result + parent.id.hashCode()
		return result
	}


}