package akuchars.domain.task.model;

import akuchars.domain.common.AbstractJpaEntity;
import akuchars.domain.common.EventBus;
import akuchars.domain.task.event.TaskChangedAssigneeAsyncEvent;
import akuchars.domain.task.event.TaskCreatedAsyncEvent;
import akuchars.domain.task.repository.ProjectTaskRepository;
import akuchars.domain.user.model.User;
import akuchars.kernel.ApplicationProperties;
import kotlin.jvm.internal.Intrinsics;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import static akuchars.kernel.ApplicationProperties.TASK_QUEUE_NAME;

@Entity
@Table(schema = ApplicationProperties.TASK_SCHEMA_NAME, name = "tasks")
public class Task extends AbstractJpaEntity {
	@OneToOne
	@JoinColumn(name = "creator_id")
	private User creator;

	@OneToOne
	@JoinColumn(name = "assignee_id")
	private User assignee;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "value", column = @Column(name = "content"))
	})
	private TaskContent taskContent;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "value", column = @Column(name = "title"))
	})
	private TaskTitle taskTitle;

	@Enumerated(EnumType.STRING)
	private TaskPriority priority;

	@Enumerated(EnumType.STRING)
	private TaskStatus status;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Project parent;

	@Embedded
	@AttributeOverrides({
			@AttributeOverride(name = "createdDate", column = @Column(name = "created_time")),
			@AttributeOverride(name = "updateDate", column = @Column(name = "update_time"))
	})
	private ChangeEntityTime time;

	private Task() {
	}

	public static Task createProjectTask(EventBus eventBus, ProjectTaskRepository repository,
										 User creator,
										 User assignee,
										 TaskContent taskContent, TaskTitle taskTitle,
										 TaskPriority taskPriority,
										 Project parent
	) {
		Intrinsics.checkParameterIsNotNull(eventBus, "eventBus");
		Intrinsics.checkParameterIsNotNull(creator, "creator");
		Intrinsics.checkParameterIsNotNull(assignee, "assignee");
		Intrinsics.checkParameterIsNotNull(taskContent, "content");
		Intrinsics.checkParameterIsNotNull(taskTitle, "title");
		Intrinsics.checkParameterIsNotNull(taskPriority, "priority");
		Intrinsics.checkParameterIsNotNull(parent, "parent");

		Task task = new Task();
		task.creator = creator;
		task.assignee = assignee;
		task.taskContent = taskContent;
		task.taskTitle = taskTitle;
		task.priority = taskPriority;
		task.parent = parent;
		task.status = TaskStatus.NEW;

		repository.save(task);
		eventBus.sendAsync(TASK_QUEUE_NAME, new TaskCreatedAsyncEvent(task.id, task.taskTitle.getValue()));
		return task;
	}

	public Task changeAssignee(final EventBus eventBus, final User assignee) {
		Intrinsics.checkParameterIsNotNull(eventBus, "eventBus");
		Intrinsics.checkParameterIsNotNull(assignee, "assignee");
		this.assignee = assignee;
		this.time.updateTime();
		eventBus.sendAsync(TASK_QUEUE_NAME, new TaskChangedAssigneeAsyncEvent(id, assignee.getId()));
		return this;
	}
}
