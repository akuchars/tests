package akuchars.domain.common

import io.vavr.control.Either

abstract class ChangeEntityPolicy<A, E : AbstractJpaEntity> {
	fun canChangeAttribute(entity: E, attribute: A): Either<out DomainException, E> {
		return if (canChangeAttributeInner(attribute, entity)) Either.right(entity)
		else Either.left(createException(attribute, entity))
	}

	protected abstract fun createException(attribute: A, task: E): DomainException
	protected abstract fun canChangeAttributeInner(attribute: A, task: E): Boolean
}
