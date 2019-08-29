package akuchars.domain.task.repository

import akuchars.domain.task.model.Project
import akuchars.domain.user.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectRepository : PagingAndSortingRepository<Project, Long> {
	fun findAllByOwner(pageable: Pageable, actualUser: User): Page<Project>
	fun findByIdAndOwner(id: Long, actualUser: User): Project?
}