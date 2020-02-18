package akuchars.motivation.application.model

class MotivationQuatationsForm(
		val sourceMotivation: Long?,
		val motivationText: String,
		val author: String,
		val active: Boolean
)

class CollectionOfMotivationQuatationsForm(
		val addressBookId: Long,
		val motivations: List<MotivationQuatationsForm>
)