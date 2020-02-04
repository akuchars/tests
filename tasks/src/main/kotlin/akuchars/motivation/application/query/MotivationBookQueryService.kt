package akuchars.motivation.application.query

import akuchars.motivation.application.model.MotivationBooksDto

interface MotivationBookQueryService {

	fun findAllMotivationBook(): MotivationBooksDto
}