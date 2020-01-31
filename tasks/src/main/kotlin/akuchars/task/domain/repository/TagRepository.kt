package akuchars.task.domain.repository

import akuchars.task.domain.model.Tag
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface TagRepository : CrudRepository<Tag, Long> {
	fun findByIdOrName(id: Long?, name: String): Optional<Tag>
}