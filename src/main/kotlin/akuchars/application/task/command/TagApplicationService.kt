package akuchars.application.task.command

import akuchars.application.task.model.TaskForm
import akuchars.domain.task.model.Tag
import akuchars.domain.task.repository.TagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TagApplicationService(private val tagRepository: TagRepository) {

	@Transactional
	fun findOrCreateTags(taskForm: TaskForm): MutableSet<Tag> {
		return taskForm.tags.map {
			val tagMaybe = tagRepository.findByIdOrName(it.id, it.name)
			if (tagMaybe.isPresent) tagMaybe.get() else tagRepository.save(Tag(it.name))
		}.toMutableSet()
	}
}