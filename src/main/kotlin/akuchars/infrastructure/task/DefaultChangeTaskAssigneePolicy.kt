package akuchars.infrastructure.task

import akuchars.domain.task.model.Task
import akuchars.domain.task.repository.ChangeTaskAssigneePolicy
import akuchars.domain.user.model.User
import org.springframework.stereotype.Component

@Component
class DefaultChangeTaskAssigneePolicy : ChangeTaskAssigneePolicy() {

	override fun canChangeAssigneeForTaskInner(task: Task, assignee: User): Boolean =
			task.parent.users.any { it.id == assignee.id }
}