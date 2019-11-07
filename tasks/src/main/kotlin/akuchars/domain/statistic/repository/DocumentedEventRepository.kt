package akuchars.domain.statistic.repository

import akuchars.domain.statistic.model.DocumentedEvent
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface DocumentedEventRepository : MongoRepository<DocumentedEvent<*>, String>