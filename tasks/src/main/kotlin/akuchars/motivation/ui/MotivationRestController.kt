package akuchars.motivation.ui

import akuchars.common.application.model.FrontDto
import akuchars.motivation.application.model.CollectionOfMotivationQuatationsForm
import akuchars.motivation.application.model.MotivationDataDto
import akuchars.motivation.application.query.MotivationQueryService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/motivations")
class MotivationRestController(private val motivationQueryService: MotivationQueryService) {

	@GetMapping
	fun getAllMotivationsForCurrentUser(@RequestParam page: Int, @RequestParam size: Int): FrontDto<Page<MotivationDataDto>> {
		val pageable = PageRequest.of(page, size)
		return motivationQueryService.finMotivationsForUser(pageable)
	}

	@PostMapping("/create")
	fun createOrEditForCurrentUser(@RequestBody form: CollectionOfMotivationQuatationsForm) {
		motivationQueryService.createOrEdit(form)
	}
}