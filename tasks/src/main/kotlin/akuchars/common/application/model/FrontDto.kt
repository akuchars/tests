package akuchars.common.application.model

import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.ResponseEntity

class FrontDto<T>(
		val body: T? = null,
		val errorDto: ErrorDto? = null
) {
	init {
		assert(listOf(body, errorDto).all { it == null })
	}

	fun toResponseEntity(): ResponseEntity<FrontDto<T>> {
		return if (errorDto == null) ResponseEntity.ok(this)
		else ResponseEntity(this, INTERNAL_SERVER_ERROR)
	}
}