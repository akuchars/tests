package akuchars.task.infrastructure

import akuchars.common.domain.DomainException
import akuchars.task.domain.exceptions.CannotFinishSubtaskOfTaskException
import akuchars.task.domain.model.Subtask
import akuchars.task.domain.model.Task
import akuchars.task.domain.repository.FinishSubtaskPolicy
import org.springframework.stereotype.Component

@Component
class DefaultFinishSubtaskPolicy : FinishSubtaskPolicy() {
	override fun createException(attribute: Subtask, task: Task): DomainException =
			CannotFinishSubtaskOfTaskException(attribute, task)

	override fun canChangeAttributeInner(attribute: Subtask, task: Task): Boolean =
			task.subtasks.any { it.id == attribute.id } && !attribute.isDone()
}