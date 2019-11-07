package akuchars.infrastructure.task

import akuchars.domain.task.exceptions.CannotChangePeriodOfTaskException
import akuchars.domain.task.exceptions.DomainTaskException
import akuchars.domain.task.model.PeriodOfTime
import akuchars.domain.task.model.Task
import akuchars.domain.task.model.TaskStatus.DONE
import akuchars.domain.task.repository.ChangePeriodAttributePolicy
import org.springframework.stereotype.Component

@Component
class DefaultChangePeriodAttributePolicy : ChangePeriodAttributePolicy() {
	override fun createException(attribute: PeriodOfTime, task: Task): DomainTaskException =
			CannotChangePeriodOfTaskException(attribute, task)

	override fun canChangeAttributeInner(attribute: PeriodOfTime, task: Task): Boolean = task.status != DONE
}