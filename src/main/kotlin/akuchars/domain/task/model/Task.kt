package akuchars.domain.task.model

import akuchars.domain.common.AbstractJpaEntity
import akuchars.domain.common.EventBus
import akuchars.domain.common.updateEntity
import akuchars.domain.task.event.TaskChangedAssigneeAsyncEvent
import akuchars.domain.task.event.TaskChangedPeriodAsyncEvent
import akuchars.domain.task.model.TaskStatus.NEW
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

	fun changeAssignee(eventBus: EventBus, changePolicy: ChangeTaskAssigneeAttributePolicy, assignee: User): Task {
		return updateEntity(eventBus, changePolicy, assignee, TaskChangedAssigneeAsyncEvent(id, assignee.id!!)) {
			this.assignee = assignee
			this.changeTime.updateTime()
			this
		}
	}

	fun changePeriod(eventBus: EventBus, changePeriodPolicy: ChangePeriodAttributePolicy, period: PeriodOfTime): Task {
		return updateEntity(eventBus, changePeriodPolicy, period, TaskChangedPeriodAsyncEvent(period)) {
			this.period = period
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
