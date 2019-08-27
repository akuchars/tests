package akuchars.domain.task.model;

import akuchars.domain.common.AbstractJpaEntity;
import akuchars.domain.common.EventBus;
import akuchars.domain.task.event.ProjectCreatedAsyncEvent;
import akuchars.domain.task.event.TaskAddedToProjectAsyncEvent;
import akuchars.domain.task.repository.AddTaskToProjectPolicy;
import akuchars.domain.task.repository.ProjectRepository;
import akuchars.domain.user.model.User;
import akuchars.kernel.ApplicationProperties;
import kotlin.jvm.internal.Intrinsics;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

import static akuchars.kernel.ApplicationProperties.TASK_QUEUE_NAME;

@Entity
@Table(schema = ApplicationProperties.TASK_SCHEMA_NAME, name = "projects")
public class Project extends AbstractJpaEntity {
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "value", column = @Column(name = "name"))
	})
	private ProjectName name;

	@OneToMany(
			mappedBy = "parent",
			cascade = {CascadeType.ALL},
			orphanRemoval = true
	)
	private Set<Task> tasks;

	@OneToOne
	@JoinColumn(name = "owner_id")
	private User owner;

	// TODO dodać użytkowników
	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "createdDate", column = @Column(name = "created_time")),
			@AttributeOverride(name = "updateDate", column = @Column(name = "update_time"))
	})
	private ChangeEntityTime changeTime;

	private Project() {
	}

	public static Project createProject(EventBus eventBus, ProjectRepository projectRepository,
										ProjectName name, Set<Task> tasks,
										User owner, ChangeEntityTime changeTime) {
		Intrinsics.checkParameterIsNotNull(name, "name");
		Intrinsics.checkParameterIsNotNull(tasks, "tasks");
		Intrinsics.checkParameterIsNotNull(owner, "owner");
		Intrinsics.checkParameterIsNotNull(changeTime, "changeTime");
		Project project = new Project();
		project.name = name;
		project.tasks = tasks;
		project.owner = owner;
		project.changeTime = changeTime;
		projectRepository.save(project);
		eventBus.sendAsync(TASK_QUEUE_NAME, new ProjectCreatedAsyncEvent(project.id, project.name.getValue()));
		return project;
	}

	public static Project createProject(EventBus eventBus, ProjectRepository projectRepository, ProjectName name, User owner) {
		return createProject(eventBus, projectRepository, name, new HashSet<>(), owner, ChangeEntityTime.now());
	}

	public Project addTask(EventBus eventBus, AddTaskToProjectPolicy addTaskToProjectPolicy, Task task) {
		if(addTaskToProjectPolicy.canAddTaskToProject(task, this)) {
			this.tasks.add(task);
			eventBus.sendAsync(TASK_QUEUE_NAME, new TaskAddedToProjectAsyncEvent(task.getId(), id));
		}
		return this;
	}

	public ProjectName getName() {
		return name;
	}

	public Set<Task> getTasks() {
		return tasks;
	}

	public User getOwner() {
		return owner;
	}
}
