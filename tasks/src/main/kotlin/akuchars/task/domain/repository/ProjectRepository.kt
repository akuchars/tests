package akuchars.task.domain.repository

import akuchars.task.domain.model.Project
import akuchars.user.domain.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface ProjectRepository : PagingAndSortingRepository<Project, Long> {
	fun findAllByOwner(pageable: Pageable, actualUser: User): Page<Project>
	fun findByIdAndOwner(id: Long, actualUser: User): Project?
}