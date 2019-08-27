package akuchars.domain.task.model

import akuchars.domain.common.AbstractJpaEntity
import akuchars.domain.common.EventBus
import akuchars.domain.task.event.ProjectCreatedAsyncEvent
import akuchars.domain.task.event.TaskAddedToProjectAsyncEvent
import akuchars.domain.task.repository.AddTaskToProjectPolicy
import akuchars.domain.task.repository.ProjectRepository
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
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(schema = ApplicationProperties.TASK_SCHEMA_NAME, name = "projects")
class Project private constructor() : AbstractJpaEntity() {
	@Embedded
	@AttributeOverrides(AttributeOverride(name = "value", column = Column(name = "name")))
	lateinit var name: ProjectName
		private set

	@OneToMany(mappedBy = "parent", cascade = [CascadeType.ALL], orphanRemoval = true)
	lateinit var tasks: MutableSet<Task>
		private set

	@OneToOne
	@JoinColumn(name = "owner_id")
	lateinit var owner: User
		private set

	// TODO dodać użytkowników
	@Embedded
	@AttributeOverrides(AttributeOverride(name = "createdDate", column = Column(name = "created_time")), AttributeOverride(name = "updateDate", column = Column(name = "update_time")))
	private lateinit var changeTime: ChangeEntityTime

	fun addTask(eventBus: EventBus, addTaskToProjectPolicy: AddTaskToProjectPolicy, task: Task): Project {
		if (addTaskToProjectPolicy.canAddTaskToProject(task, this)) {
			this.tasks.add(task)
			eventBus.sendAsync(TASK_QUEUE_NAME, TaskAddedToProjectAsyncEvent(task.id!!, id))
		}
		return this
	}

	companion object {

		fun createProject(eventBus: EventBus, projectRepository: ProjectRepository,
						  name: ProjectName, tasks: MutableSet<Task>,
						  owner: User, changeTime: ChangeEntityTime): Project {
			return Project().apply {
				this.name = name
				this.tasks = tasks
				this.owner = owner
				this.changeTime = changeTime
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
