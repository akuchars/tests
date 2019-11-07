package akuchars.domain.email.repository

import akuchars.domain.email.model.EmailTemplateLocation
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmailTemplateLocationRepository : CrudRepository<EmailTemplateLocation, String> {
	fun findByKey(key: String): EmailTemplateLocation?
}