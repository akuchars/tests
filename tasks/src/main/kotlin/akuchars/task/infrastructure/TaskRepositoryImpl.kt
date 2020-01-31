package akuchars.task.infrastructure

import akuchars.task.domain.model.Project
import akuchars.task.domain.model.Tag
import akuchars.task.domain.model.Task
import akuchars.task.domain.repository.CustomTaskRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager

@Repository
class TaskRepositoryImpl(
		private val entityManager: EntityManager
) : CustomTaskRepository {

	override fun findByTagAndProject(pageable: Pageable, tag: Tag, project: Project?): Page<Task> {
		val cb = entityManager.criteriaBuilder
		val query = cb.createQuery(Task::class.java)
		val task = query.from(Task::class.java)

		query.select(task)
				.where(
						cb.equal(task.get<Project>("parent"), project),
						cb.isMember(tag, task.get<Set<Tag>>("tags"))
				)

		return PageImpl(entityManager.createQuery(query).resultList, pageable, 100)
	}
}