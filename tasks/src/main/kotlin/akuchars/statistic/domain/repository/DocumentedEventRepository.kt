package akuchars.statistic.domain.repository

import akuchars.statistic.domain.model.DocumentedEvent
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface DocumentedEventRepository : MongoRepository<DocumentedEvent<*>, String>