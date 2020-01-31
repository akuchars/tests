package akuchars.task.ui

import akuchars.common.application.model.FrontDto
import akuchars.task.application.command.TaskApplicationService
import akuchars.task.application.model.TaskDto
import akuchars.task.application.model.TaskForm
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
	fun createNewTask(@RequestBody taskForm: TaskForm): ResponseEntity<FrontDto<TaskDto>> =
			taskApplicationService.createNewTask(taskForm).toResponseEntity()

	@PutMapping("/edit")
	@ResponseStatus(HttpStatus.CREATED)
	fun editTask(@RequestBody taskForm: TaskForm): ResponseEntity<FrontDto<TaskDto>> =
			taskApplicationService.editTask(taskForm).toResponseEntity()

	@PostMapping("/{taskId}/assignee/{userId}")
	fun changeAssignee(@PathVariable taskId: Long, @PathVariable userId: Long) : ResponseEntity<FrontDto<TaskDto>> =
			taskApplicationService.changeAssigneeForTask(taskId, userId).toResponseEntity()

	@PostMapping("/{taskId}/finish/{subtaskId}")
	fun finishSubtask(@PathVariable taskId: Long, @PathVariable subtaskId: Long) : ResponseEntity<FrontDto<TaskDto>> =
			taskApplicationService.finishSubtask(taskId, subtaskId).toResponseEntity()
}