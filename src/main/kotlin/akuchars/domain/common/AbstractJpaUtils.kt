package akuchars.domain.common

import akuchars.kernel.ApplicationProperties.TASK_QUEUE_NAME

fun <A, E : AbstractJpaEntity> E.updateEntityWithPolicy(eventBus: EventBus,
                                                        changePolicy: ChangeEntityPolicy<A, E>,
                                                        attribute: A,
                                                        asyncEvent: AsyncEvent,
                                                        action: (E) -> E): E {
	val canChange = changePolicy.canChangeAttribute(this, attribute)
	if (canChange.isRight) {
		val entity = action.invoke(this)
		eventBus.sendAsync(TASK_QUEUE_NAME, asyncEvent)
		return entity
	} else throw canChange.left
}

fun <E : AbstractJpaEntity> E.updateEntity(eventBus: EventBus,
                                           asyncEvent: AsyncEvent,
                                           action: (E) -> E): E {
	val entity = action.invoke(this)
	eventBus.sendAsync(TASK_QUEUE_NAME, asyncEvent)
	return entity
}
