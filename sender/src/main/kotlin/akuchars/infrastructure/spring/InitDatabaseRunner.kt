package akuchars.infrastructure.spring

import akuchars.domain.email.model.EmailTemplateLocation
import akuchars.domain.email.repository.EmailTemplateLocationRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class InitDatabaseRunner(private val emailTemplateLocationRepository: EmailTemplateLocationRepository) : ApplicationRunner {

	@Transactional
	override fun run(args: ApplicationArguments) {
		emailTemplateLocationRepository.save(EmailTemplateLocation("DUPA", "dupa", "Some nice title"))
	}
}