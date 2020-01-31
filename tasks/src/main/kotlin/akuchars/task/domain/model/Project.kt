package akuchars.task.domain.model

import akuchars.common.domain.AbstractJpaEntity
import akuchars.common.domain.EventBus
import akuchars.common.kernel.ApplicationProperties
import akuchars.common.kernel.ApplicationProperties.TASK_QUEUE_NAME
import akuchars.task.domain.event.ProjectCreatedAsyncEvent
import akuchars.task.domain.event.TaskAddedToProjectAsyncEvent
import akuchars.task.domain.repository.AddTaskToProjectPolicy
import akuchars.task.domain.repository.ProjectRepository
import akuchars.task.domain.repository.TaskRepository
import akuchars.user.domain.model.User
import java.util.HashSet
import javax.persistence.AttributeOverride
import javax.persistence.AttributeOverrides
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.FetchType.EAGER
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(schema = ApplicationProperties.TASK_SCHEMA_NAME, name = "projects")
data class Project(
		@Embedded
		@AttributeOverrides(AttributeOverride(name = "value", column = Column(name = "name")))
		val name: ProjectName,

		@OneToMany(mappedBy = "parent", cascade = [CascadeType.ALL], orphanRemoval = true)
		val tasks: MutableSet<Task>,

		@ManyToOne
		@JoinColumn(name = "owner_id")
		val owner: User,

		@Embedded
		@AttributeOverrides(
				AttributeOverride(name = "startDate", column = Column(name = "created_time")),
				AttributeOverride(name = "endDate", column = Column(name = "update_time"))
		)
		val changeTime: PeriodOfTime,

		@ManyToMany(fetch = EAGER)
		@JoinTable(
				schema = ApplicationProperties.TASK_SCHEMA_NAME,
				name = "projects_users",
				joinColumns = [JoinColumn(name = "project_id")],
				inverseJoinColumns = [JoinColumn(name = "user_id")]
		)
		val users: Set<User>
) : AbstractJpaEntity() {

	fun addTask(eventBus: EventBus, taskRepository: TaskRepository, task: Task, addTaskToProjectPolicy: AddTaskToProjectPolicy): Project {
		val policyResult = addTaskToProjectPolicy.canAddTaskToProject(task, this)
		if (policyResult.isRight) {
			task.parent = this
			val updatedTask = taskRepository.save(task)
			eventBus.sendAsync(TASK_QUEUE_NAME, TaskAddedToProjectAsyncEvent(updatedTask.id!!, id))
			return this
		} else throw policyResult.left
	}

	fun addTask(eventBus: EventBus, taskRepository: TaskRepository, task: Task, canAddTaskPolicyFunction: (Task, Project) -> Boolean): Project {
		return addTask(eventBus, taskRepository, task, object : AddTaskToProjectPolicy() {
			override fun canAddTaskToProjectInner(task: Task, project: Project): Boolean =
					canAddTaskPolicyFunction.invoke(task, project)
		})
	}

	companion object {

		fun createProject(eventBus: EventBus, projectRepository: ProjectRepository,
		                  name: ProjectName, tasks: MutableSet<Task>,
		                  owner: User, changeTime: PeriodOfTime): Project {
			return Project(name, tasks, owner, changeTime, setOf(owner)).apply {
				projectRepository.save(this)
			}.also {
				eventBus.sendAsync(TASK_QUEUE_NAME, ProjectCreatedAsyncEvent(it.id, it.name.value))
			}
		}

		fun createProject(eventBus: EventBus, projectRepository: ProjectRepository, name: ProjectName, owner: User): Project {
			return createProject(eventBus, projectRepository, name, HashSet(), owner, PeriodOfTime.now())
		}
	}
}
