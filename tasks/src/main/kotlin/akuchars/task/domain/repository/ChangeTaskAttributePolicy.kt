package akuchars.task.domain.repository

import akuchars.common.domain.ChangeEntityPolicy
import akuchars.task.domain.model.PeriodOfTime
import akuchars.task.domain.model.Subtask
import akuchars.task.domain.model.Tag
import akuchars.task.domain.model.Task
import akuchars.user.domain.model.User

sealed class ChangeTaskAttributePolicy<A> : ChangeEntityPolicy<A, Task>()

abstract class ChangeTaskAssigneeAttributePolicy : ChangeTaskAttributePolicy<User>()

abstract class ChangePeriodAttributePolicy : ChangeTaskAttributePolicy<PeriodOfTime>()

abstract class AddTagToTaskAttributePolicy : ChangeTaskAttributePolicy<Tag>()

abstract class FinishSubtaskPolicy : ChangeTaskAttributePolicy<Subtask>()