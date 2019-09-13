package akuchars.domain.task.model

import akuchars.domain.common.AbstractJpaEntity
import akuchars.domain.common.EventBus
import akuchars.domain.common.updateEntity
import akuchars.domain.common.updateEntityWithPolicy
import akuchars.domain.task.event.TagAddedToTaskAsyncEvent
import akuchars.domain.task.event.TaskChangedAssigneeAsyncEvent
import akuchars.domain.task.event.TaskChangedPeriodAsyncEvent
import akuchars.domain.task.event.TaskEditedAssigneeAsyncEvent
import akuchars.domain.task.model.TaskStatus.NEW
import akuchars.domain.task.repository.AddTagToTaskAttributePolicy
import akuchars.domain.task.repository.ChangePeriodAttributePolicy
import akuchars.domain.task.repository.ChangeTaskAssigneeAttributePolicy
import akuchars.domain.user.model.User
import akuchars.kernel.ApplicationProperties
import javax.persistence.AttributeOverride
import javax.persistence.AttributeOverrides
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType.EAGER
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
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
		var taskContent: TaskContent,

		@Embedded
		@AttributeOverrides(AttributeOverride(name = "value", column = Column(name = "title")))
		var taskTitle: TaskTitle,

		@Enumerated(EnumType.STRING)
		var priority: TaskPriority,

		@Enumerated(EnumType.STRING)
		val status: TaskStatus,

		@Embedded
		@AttributeOverrides(
				AttributeOverride(name = "startDate", column = Column(name = "created_time")),
				AttributeOverride(name = "endDate", column = Column(name = "update_time"))
		)
		val changeTime: PeriodOfTime

) : AbstractJpaEntity() {

	@ManyToOne
	@JoinColumn(name = "parent_id")
	lateinit var parent: Project

	@Embedded
	@AttributeOverrides(
			AttributeOverride(name = "startDate", column = Column(name = "start_time")),
			AttributeOverride(name = "endDate", column = Column(name = "end_time"))
	)
	var period: PeriodOfTime? = null

	@Embedded
	@AttributeOverrides(AttributeOverride(name = "value", column = Column(name = "main_goal")))
	var mainGoal: TaskMainGoal? = null

	@ManyToMany(fetch = EAGER)
	@JoinTable(
			schema = ApplicationProperties.TASK_SCHEMA_NAME,
			name = "task_tags",
			joinColumns = [JoinColumn(name = "task_id")],
			inverseJoinColumns = [JoinColumn(name = "tag_id")]
	)
	var tags: MutableSet<Tag> = mutableSetOf()

	fun changeTaskData(eventBus: EventBus, taskContent: TaskContent,
	                   taskTitle: TaskTitle,
	                   taskPriority: TaskPriority
	): Task {
		return updateEntity(eventBus, TaskEditedAssigneeAsyncEvent(id, taskTitle.value)) {
			this.taskContent = taskContent
			this.taskTitle = taskTitle
			this.priority = taskPriority
			this.changeTime.updateTime()
			this
		}
	}

	fun changeAssignee(eventBus: EventBus, changePolicy: ChangeTaskAssigneeAttributePolicy, assignee: User): Task {
		return updateEntityWithPolicy(eventBus, changePolicy, assignee, TaskChangedAssigneeAsyncEvent(id, assignee.id!!)) {
			this.assignee = assignee
			this.changeTime.updateTime()
			this
		}
	}

	fun changePeriod(eventBus: EventBus, changePeriodPolicy: ChangePeriodAttributePolicy, period: PeriodOfTime): Task {
		return updateEntityWithPolicy(eventBus, changePeriodPolicy, period, TaskChangedPeriodAsyncEvent(period)) {
			this.period = period
			this.changeTime.updateTime()
			this
		}
	}

	fun addTag(eventBus: EventBus, addTagToTaskAttributePolicy: AddTagToTaskAttributePolicy, tag: Tag): Task {
		return updateEntityWithPolicy(eventBus, addTagToTaskAttributePolicy, tag, TagAddedToTaskAsyncEvent(tag.name, taskTitle.value)) {
			this.tags.add(tag)
			this.changeTime.updateTime()
			this
		}
	}

	companion object {

		fun createProjectTask(creator: User,
		                      assignee: User,
		                      taskContent: TaskContent,
		                      taskTitle: TaskTitle, taskPriority: TaskPriority
		): Task {
			return Task(creator, assignee, taskContent, taskTitle, taskPriority, NEW, PeriodOfTime.now())
		}
	}
}
