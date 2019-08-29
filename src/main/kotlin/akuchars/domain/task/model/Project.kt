package akuchars.domain.task.model

import akuchars.domain.common.AbstractJpaEntity
import akuchars.domain.common.EventBus
import akuchars.domain.task.event.ProjectCreatedAsyncEvent
import akuchars.domain.task.event.TaskAddedToProjectAsyncEvent
import akuchars.domain.task.repository.AddTaskToProjectPolicy
import akuchars.domain.task.repository.ProjectRepository
import akuchars.domain.task.repository.ProjectTaskRepository
import akuchars.domain.user.model.User
import akuchars.kernel.ApplicationProperties
import akuchars.kernel.ApplicationProperties.TASK_QUEUE_NAME
import java.util.HashSet
import javax.persistence.AttributeOverride
import javax.persistence.AttributeOverrides
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.JoinColumn
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
				AttributeOverride(name = "createdDate", column = Column(name = "created_time")),
				AttributeOverride(name = "updateDate", column = Column(name = "update_time"))
		)
		val changeTime: ChangeEntityTime
) : AbstractJpaEntity() {
	// TODO dodać użytkowników

	fun addTask(eventBus: EventBus, taskRepository: ProjectTaskRepository, task: Task, addTaskToProjectPolicy: AddTaskToProjectPolicy): Project {
		if (addTaskToProjectPolicy.canAddTaskToProject(task, this)) {
			task.parent = this
			val updatedTask = taskRepository.save(task)
			eventBus.sendAsync(TASK_QUEUE_NAME, TaskAddedToProjectAsyncEvent(updatedTask.id!!, id))
		}
		return this
	}

	fun addTask(eventBus: EventBus, taskRepository: ProjectTaskRepository, task: Task, canAddTaskPolicyFunction: (Task, Project) -> Boolean): Project {
		return addTask(eventBus, taskRepository, task, object : AddTaskToProjectPolicy {
			override fun canAddTaskToProjectInner(task: Task, project: Project): Boolean =
					canAddTaskPolicyFunction.invoke(task, project)
		})
	}

	companion object {

		fun createProject(eventBus: EventBus, projectRepository: ProjectRepository,
		                  name: ProjectName, tasks: MutableSet<Task>,
		                  owner: User, changeTime: ChangeEntityTime): Project {
			return Project(name, tasks, owner, changeTime).apply {
				projectRepository.save(this)
			}.also {
				eventBus.sendAsync(TASK_QUEUE_NAME, ProjectCreatedAsyncEvent(it.id, it.name.value))
			}
		}

		fun createProject(eventBus: EventBus, projectRepository: ProjectRepository, name: ProjectName, owner: User): Project {
			return createProject(eventBus, projectRepository, name, HashSet(), owner, ChangeEntityTime.now())
		}
	}
}
