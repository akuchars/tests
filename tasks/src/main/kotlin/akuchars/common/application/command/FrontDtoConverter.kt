package akuchars.common.application.command

import akuchars.common.application.model.ErrorDto
import akuchars.common.application.model.FrontDto
import akuchars.common.domain.DomainException
import akuchars.common.kernel.logger
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component

@Component
class FrontDtoConverter(
		private val messageSource: MessageSource
) {
	fun resolveMessageFromCode(exception: DomainException): String =
			messageSource.getMessage(exception.frontErrorCode, exception.getMessageResolverParams(), LocaleContextHolder.getLocale())

	fun <T> toFrontDto(runner: () -> T): FrontDto<T> {
		val result = runCatching { runner.invoke() }
		return if (result.isSuccess) FrontDto(result.getOrNull()!!)
		else {
			val exception = result.exceptionOrNull()!!
			logger.error(exception.stackTrace?.contentDeepToString())
			exception.printStackTrace()
			when (exception) {
				is DomainException -> FrontDto(errorDto = ErrorDto(exception::class.java.simpleName, resolveMessageFromCode(exception)))
				else -> FrontDto(errorDto = ErrorDto(exception.toString(), exception.message
						?: ""))
			}
		}
	}
}
