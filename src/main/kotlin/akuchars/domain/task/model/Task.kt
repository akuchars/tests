package akuchars.domain.task.model

import akuchars.domain.common.AbstractJpaEntity
import akuchars.domain.common.EventBus
import akuchars.domain.task.event.TaskChangedAssigneeAsyncEvent
import akuchars.domain.task.event.TaskCreatedAsyncEvent
import akuchars.domain.task.repository.ProjectTaskRepository
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
class Task private constructor() : AbstractJpaEntity() {
	@OneToOne
	@JoinColumn(name = "creator_id")
	lateinit var creator: User
		private set

	@OneToOne
	@JoinColumn(name = "assignee_id")
	lateinit var assignee: User
		private set

	@Embedded
	@AttributeOverrides(AttributeOverride(name = "value", column = Column(name = "content")))
	lateinit var taskContent: TaskContent
		private set

	@Embedded
	@AttributeOverrides(AttributeOverride(name = "value", column = Column(name = "title")))
	lateinit var taskTitle: TaskTitle
		private set

	@Enumerated(EnumType.STRING)
	lateinit var priority: TaskPriority
		private set

	@Enumerated(EnumType.STRING)
	lateinit var status: TaskStatus
		private set

	@ManyToOne
	@JoinColumn(name = "parent_id")
	lateinit var parent: Project
		private set

	@Embedded
	@AttributeOverrides(AttributeOverride(name = "createdDate", column = Column(name = "created_time")), AttributeOverride(name = "updateDate", column = Column(name = "update_time")))
	lateinit var time: ChangeEntityTime
		private set

	fun changeAssignee(eventBus: EventBus, assignee: User): Task {
		this.assignee = assignee
		this.time.updateTime()
		eventBus.sendAsync(TASK_QUEUE_NAME, TaskChangedAssigneeAsyncEvent(id, assignee.id!!))
		return this
	}

	companion object {

		fun createProjectTask(eventBus: EventBus, repository: ProjectTaskRepository,
							  creator: User,
							  assignee: User,
							  taskContent: TaskContent, taskTitle: TaskTitle,
							  taskPriority: TaskPriority,
							  parent: Project
		): Task {
			return Task().apply {
				this.creator = creator
				this.assignee = assignee
				this.taskContent = taskContent
				this.taskTitle = taskTitle
				this.priority = taskPriority
				this.parent = parent
				this.status = TaskStatus.NEW
				this.time = ChangeEntityTime.now()

				repository.save(this)
			}.also {
				eventBus.sendAsync(TASK_QUEUE_NAME, TaskCreatedAsyncEvent(it.id, it.taskTitle.value))
			}
		}
	}
}
