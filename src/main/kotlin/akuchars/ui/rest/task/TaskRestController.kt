package akuchars.ui.rest.task

import akuchars.application.common.model.FrontDto
import akuchars.application.task.command.TaskApplicationService
import akuchars.application.task.model.TaskDto
import akuchars.application.task.model.TaskForm
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/task/tasks")
class TaskRestController(private val taskApplicationService: TaskApplicationService) {

	@PutMapping("/create")
	@ResponseStatus(HttpStatus.CREATED)
	fun createNewTask(@RequestBody taskForm: TaskForm): FrontDto<TaskDto> =
			taskApplicationService.createNewTask(taskForm)

	@PostMapping("/{taskId}/assignee/{userId}")
	fun changeAssignee(@PathVariable taskId: Long, @PathVariable userId: Long) : FrontDto<TaskDto> =
			taskApplicationService.changeAssigneeForTask(taskId, userId)
}