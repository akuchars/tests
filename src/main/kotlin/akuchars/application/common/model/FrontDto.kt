package akuchars.application.common.model

class FrontDto<T>(
		val body: T? = null,
		val errorDto: ErrorDto? = null
) {
	init {
		assert(listOf(body, errorDto).all { it == null })
	}
}