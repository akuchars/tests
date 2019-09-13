package akuchars.infrastructure.task

import akuchars.domain.common.DomainException
import akuchars.domain.task.model.Tag
import akuchars.domain.task.model.Task
import akuchars.domain.task.repository.AddTagToTaskAttributePolicy
import org.springframework.stereotype.Component

@Component
class DefaultAddTagToTaskAttributePolicy : AddTagToTaskAttributePolicy() {
	override fun createException(attribute: Tag, task: Task): DomainException {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun canChangeAttributeInner(attribute: Tag, task: Task): Boolean = true
}