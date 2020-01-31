package akuchars.notification.email.domain.repository

import akuchars.notification.email.domain.model.EmailTemplateLocation
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmailTemplateLocationRepository : CrudRepository<EmailTemplateLocation, String> {
	fun findByKey(key: String): EmailTemplateLocation?
}