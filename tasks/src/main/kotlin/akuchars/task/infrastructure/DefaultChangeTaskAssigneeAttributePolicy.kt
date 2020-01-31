package akuchars.task.infrastructure

import akuchars.task.domain.exceptions.CannotChangeAssigneeForTaskException
import akuchars.task.domain.exceptions.DomainTaskException
import akuchars.task.domain.model.Task
import akuchars.task.domain.repository.ChangeTaskAssigneeAttributePolicy
import akuchars.user.domain.model.User
import org.springframework.stereotype.Component

@Component
class DefaultChangeTaskAssigneeAttributePolicy : ChangeTaskAssigneeAttributePolicy() {
	override fun createException(attribute: User, task: Task): DomainTaskException =
			CannotChangeAssigneeForTaskException(attribute, task)

	override fun canChangeAttributeInner(attribute: User, task: Task): Boolean {
		return task.parent.users.any { it.id == attribute.id }
	}
}