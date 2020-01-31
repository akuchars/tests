package akuchars.task.infrastructure

import akuchars.task.domain.exceptions.CannotChangePeriodOfTaskException
import akuchars.task.domain.exceptions.DomainTaskException
import akuchars.task.domain.model.PeriodOfTime
import akuchars.task.domain.model.Task
import akuchars.task.domain.model.TaskStatus.DONE
import akuchars.task.domain.repository.ChangePeriodAttributePolicy
import org.springframework.stereotype.Component

@Component
class DefaultChangePeriodAttributePolicy : ChangePeriodAttributePolicy() {
	override fun createException(attribute: PeriodOfTime, task: Task): DomainTaskException =
			CannotChangePeriodOfTaskException(attribute, task)

	override fun canChangeAttributeInner(attribute: PeriodOfTime, task: Task): Boolean = task.status != DONE
}