package akuchars.infrastructure.task

import akuchars.domain.common.DomainException
import akuchars.domain.task.exceptions.CannotFinishSubtaskOfTaskException
import akuchars.domain.task.model.Subtask
import akuchars.domain.task.model.Task
import akuchars.domain.task.repository.FinishSubtaskPolicy
import org.springframework.stereotype.Component

@Component
class DefaultFinishSubtaskPolicy : FinishSubtaskPolicy() {
	override fun createException(attribute: Subtask, task: Task): DomainException =
			CannotFinishSubtaskOfTaskException(attribute, task)

	override fun canChangeAttributeInner(attribute: Subtask, task: Task): Boolean =
			task.subtasks.any { it.id == attribute.id } && !attribute.isDone()
}