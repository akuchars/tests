package akuchars.task.domain.model

import akuchars.common.domain.AbstractJpaEntity
import akuchars.common.domain.EventBus
import akuchars.common.domain.updateEntity
import akuchars.common.domain.updateEntityWithPolicy
import akuchars.common.kernel.ApplicationProperties
import akuchars.task.domain.event.SubtaskAddedToTaskAsyncEvent
import akuchars.task.domain.event.SubtaskCompletedInTaskAsyncEvent
import akuchars.task.domain.event.TagAddedToTaskAsyncEvent
import akuchars.task.domain.event.TaskChangedAssigneeAsyncEvent
import akuchars.task.domain.event.TaskChangedPeriodAsyncEvent
import akuchars.task.domain.event.TaskCompletedAsyncEvent
import akuchars.task.domain.event.TaskEditedAssigneeAsyncEvent
import akuchars.task.domain.model.TaskStatus.NEW
import akuchars.task.domain.repository.AddTagToTaskAttributePolicy
import akuchars.task.domain.repository.ChangePeriodAttributePolicy
import akuchars.task.domain.repository.ChangeTaskAssigneeAttributePolicy
import akuchars.task.domain.repository.FinishSubtaskPolicy
import akuchars.user.domain.model.User
import javax.persistence.AttributeOverride
import javax.persistence.AttributeOverrides
import javax.persistence.CascadeType
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
import javax.persistence.OneToMany
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
		var status: TaskStatus,

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

	@OneToMany(mappedBy = "parent", cascade = [CascadeType.ALL], orphanRemoval = true)
	var subtasks: MutableList<Subtask> = mutableListOf()

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

	fun addSubtasks(eventBus: EventBus, subtasks: MutableList<Subtask>): Task {
		return updateEntity(eventBus, SubtaskAddedToTaskAsyncEvent()) {
			this.subtasks.addAll(subtasks)
			this.changeTime.updateTime()
			this
		}
	}

	fun forceFinishTask(eventBus: EventBus, taskCompletedAsyncEvent: TaskCompletedAsyncEvent): Task {
		return updateEntity(eventBus, taskCompletedAsyncEvent) {
			this.status = TaskStatus.DONE
			this.changeTime.updateTime()
			this
		}
	}

	fun finishSubtask(eventBus: EventBus, subtask: Subtask, finishSubtaskPolicy: FinishSubtaskPolicy): Task {
		return updateEntityWithPolicy(eventBus, finishSubtaskPolicy, subtask, SubtaskCompletedInTaskAsyncEvent(subtask.id, id)) {
			this.subtasks.find { it.id == subtask.id }!!.markAsDone()
			//todo kończenie zadań globalnych powinno być uzależnione od ustawień użytkownika
			if (isAllSubtaskFinished()) forceFinishTask(eventBus, TaskCompletedAsyncEvent(id))
			else this
		}
	}

	fun recalculatePeriod(): Task {
		return apply {
			//todo poprawić to, powinien wyliczać ten chujek następne wystąpienie
		}
	}

	fun countDoneSubtask(): Int = subtasks.count { it.isDone() }
	fun countUndoneSubtask(): Int = subtasks.count { !it.isDone() }
	private fun isAllSubtaskFinished() = this.subtasks.size == this.countDoneSubtask()

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
