package akuchars.ui.rest.task

import akuchars.application.common.model.FrontDto
import akuchars.application.task.command.ProjectApplicationService
import akuchars.application.task.model.DetailProjectDto
import akuchars.application.task.model.ProjectDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/task/projects")
class ProjectRestController(
		private val projectApplicationService: ProjectApplicationService
) {

	@ResponseStatus(HttpStatus.CREATED)
	@PutMapping("/create")
	fun createNewProject(@RequestParam projectName: String): ResponseEntity<FrontDto<ProjectDto>> {
		return projectApplicationService.createNewProject(projectName).toResponseEntity()
	}

	@GetMapping
	fun getAllProjects(@RequestParam page: Int, @RequestParam size: Int): Page<ProjectDto> {
		val pageable = PageRequest.of(page, size)
		return projectApplicationService.getProjectsPaginated(pageable)
	}

	@GetMapping("/{id}")
	fun getProjectById(@PathVariable id: Long): DetailProjectDto? {
		return projectApplicationService.findProjectById(id)
	}
}