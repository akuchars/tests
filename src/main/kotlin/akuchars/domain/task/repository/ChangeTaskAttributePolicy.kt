package akuchars.domain.task.repository

import akuchars.domain.common.ChangeEntityPolicy
import akuchars.domain.task.model.PeriodOfTime
import akuchars.domain.task.model.Task
import akuchars.domain.user.model.User

sealed class ChangeTaskAttributePolicy<A> : ChangeEntityPolicy<A, Task>()

abstract class ChangeTaskAssigneeAttributePolicy : ChangeTaskAttributePolicy<User>()

abstract class ChangePeriodAttributePolicy: ChangeTaskAttributePolicy<PeriodOfTime>()