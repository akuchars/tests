package akuchars.task.infrastructure

import akuchars.common.domain.DomainException
import akuchars.task.domain.model.Tag
import akuchars.task.domain.model.Task
import akuchars.task.domain.repository.AddTagToTaskAttributePolicy
import org.springframework.stereotype.Component

@Component
class DefaultAddTagToTaskAttributePolicy : AddTagToTaskAttributePolicy() {
	override fun createException(attribute: Tag, task: Task): DomainException {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun canChangeAttributeInner(attribute: Tag, task: Task): Boolean = true
}