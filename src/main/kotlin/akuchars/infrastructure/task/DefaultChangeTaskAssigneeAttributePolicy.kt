package akuchars.infrastructure.task

import akuchars.domain.task.exceptions.CannotChangeAssigneeForTaskException
import akuchars.domain.task.exceptions.DomainTaskException
import akuchars.domain.task.model.Task
import akuchars.domain.task.repository.ChangeTaskAssigneeAttributePolicy
import akuchars.domain.user.model.User
import org.springframework.stereotype.Component

@Component
class DefaultChangeTaskAssigneeAttributePolicy : ChangeTaskAssigneeAttributePolicy() {
	override fun createException(attribute: User, task: Task): DomainTaskException =
			CannotChangeAssigneeForTaskException(attribute, task)

	override fun canChangeAttributeInner(attribute: User, task: Task): Boolean {
		return task.parent.users.any { it.id == attribute.id }
	}
}