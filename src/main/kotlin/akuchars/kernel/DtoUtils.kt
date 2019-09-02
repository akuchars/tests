package akuchars.kernel

import akuchars.application.common.model.ErrorDto
import akuchars.application.common.model.FrontDto
import akuchars.domain.common.DomainException

fun <T> toFrontDto(runner: () -> T): FrontDto<T> {
	val result = runCatching { runner.invoke() }
	return if (result.isSuccess) FrontDto(result.getOrNull()!!)
	else {
		when (val exception = result.exceptionOrNull()!!) {
			is DomainException -> FrontDto(errorDto = ErrorDto(exception.msg, exception.frontErrorCode))
			else -> FrontDto(errorDto = ErrorDto(exception.toString(), exception.message ?: ""))
		}
	}
}
