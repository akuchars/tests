package akuchars.domain.task.model

import akuchars.domain.common.AbstractJpaEntity
import akuchars.domain.common.EventBus
import akuchars.domain.task.event.TaskChangedAssigneeAsyncEvent
import akuchars.domain.task.model.TaskStatus.NEW
import akuchars.domain.user.model.User
import akuchars.kernel.ApplicationProperties
import akuchars.kernel.ApplicationProperties.TASK_QUEUE_NAME
import javax.persistence.AttributeOverride
import javax.persistence.AttributeOverrides
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(schema = ApplicationProperties.TASK_SCHEMA_NAME, name = "tasks")
data class Task(
		@OneToOne
		@JoinColumn(name = "creator_id")
		val creator: User,

		@OneToOne
		@JoinColumn(name = "assignee_id")
		var assignee: User,

		@Embedded
		@AttributeOverrides(AttributeOverride(name = "value", column = Column(name = "content")))
		val taskContent: TaskContent,

		@Embedded
		@AttributeOverrides(AttributeOverride(name = "value", column = Column(name = "title")))
		val taskTitle: TaskTitle,

		@Enumerated(EnumType.STRING)
		val priority: TaskPriority,

		@Enumerated(EnumType.STRING)
		val status: TaskStatus,

		@Embedded
		@AttributeOverrides(AttributeOverride(name = "createdDate", column = Column(name = "created_time")), AttributeOverride(name = "updateDate", column = Column(name = "update_time")))
		val time: ChangeEntityTime

) : AbstractJpaEntity() {

	@ManyToOne
	@JoinColumn(name = "parent_id")
	lateinit var parent: Project

	fun changeAssignee(eventBus: EventBus, assignee: User): Task {
		this.assignee = assignee
		this.time.updateTime()
		eventBus.sendAsync(TASK_QUEUE_NAME, TaskChangedAssigneeAsyncEvent(id, assignee.id!!))
		return this
	}

	companion object {

		fun createProjectTask(creator: User,
		                      assignee: User,
		                      taskContent: TaskContent,
		                      taskTitle: TaskTitle, taskPriority: TaskPriority
		): Task {
			return Task(creator, assignee, taskContent, taskTitle, taskPriority, NEW, ChangeEntityTime.now())
		}
	}
}
