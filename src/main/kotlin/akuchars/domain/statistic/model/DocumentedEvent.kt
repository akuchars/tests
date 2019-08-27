package akuchars.domain.statistic.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
class DocumentedEvent<E>(@Id var id: String? = null,
					  var eventMsg: String,
					  val type: String,
					  val clazz: String,
					  val date: LocalDateTime,
					  val event: E?
)