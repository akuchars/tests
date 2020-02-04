package akuchars.notification.email.application.command

import akuchars.common.kernel.RegexUtils
import akuchars.infrastructure.spring.ThymeleafResolverConfiguration
import akuchars.notification.email.application.model.EmailTemplateDto
import akuchars.notification.email.application.model.EmailTemplateResponseDto
import akuchars.notification.email.domain.model.EmailTemplateLocation
import akuchars.notification.email.domain.repository.EmailTemplateLocationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.BufferedWriter
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@Service
internal class EmailTemplateService(
		private val emailTemplateLocationRepository: EmailTemplateLocationRepository,
		private val thymeleafResolver: ThymeleafResolverConfiguration
) {

	@Transactional
	fun saveNewTemplate(emailTemplateDto: EmailTemplateDto): EmailTemplateResponseDto {
		val path = buildPath(emailTemplateDto)
		createFileContent(path, emailTemplateDto.body)
		val emailTemplateLocation = EmailTemplateLocation(
				emailTemplateDto.code,
				path.toString(),
				emailTemplateDto.title
		)

		//todo create expectedParams by regex (from body text)
		val expectedParams = RegexUtils.PARAMS_REGEX.toRegex().find(emailTemplateDto.body)?.groupValues ?: listOf()

		emailTemplateLocationRepository.save(emailTemplateLocation)
		return EmailTemplateResponseDto(emailTemplateDto.code, expectedParams)
	}

	private fun createFileContent(path: Path, content: String) {
		Files.createFile(path).toFile().let(::FileWriter).let(::BufferedWriter)
				.also {
					it.write(content)
					it.close()
				}
	}

	private fun buildPath(emailTemplateDto: EmailTemplateDto) =
			Paths.get("C:\\Repozytoria\\akuchars\\parent\\sender\\src\\main\\resources")
					.resolve(thymeleafResolver.prefix)
					.resolve("${emailTemplateDto.code}.html")
					.toAbsolutePath()
}