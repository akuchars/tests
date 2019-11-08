package akuchars.application.email.command

import akuchars.application.email.model.EmailTemplateDto
import akuchars.application.email.model.EmailTemplateResponseDto
import akuchars.domain.email.model.EmailTemplateLocation
import akuchars.domain.email.repository.EmailTemplateLocationRepository
import akuchars.infrastructure.spring.ThymeleafResolverConfiguration
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.nio.file.Files
import java.nio.file.Paths

@Service
class EmailTemplateService(
		private val emailTemplateLocationRepository: EmailTemplateLocationRepository,
		private val thymeleafResolver: ThymeleafResolverConfiguration
) {

	@Transactional
	fun saveNewTemplate(emailTemplateDto: EmailTemplateDto): EmailTemplateResponseDto {
		val path = buildPath(emailTemplateDto)
		Files.createFile(path)
		val emailTemplateLocation = EmailTemplateLocation(
				emailTemplateDto.code,
				path.toString(),
				emailTemplateDto.title
		)

		emailTemplateLocationRepository.save(emailTemplateLocation)
		//todo zmienić żeby tworzył listę expectedParams
		return EmailTemplateResponseDto(emailTemplateDto.code, listOf())
	}

	private fun buildPath(emailTemplateDto: EmailTemplateDto) =
			Paths.get("C:\\Repozytoria\\akuchars\\parent\\sender\\src\\main\\resources")
					.resolve(thymeleafResolver.prefix).resolve("${emailTemplateDto.code}.html")
}