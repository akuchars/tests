package akuchars.motivation.domain.model

enum class MotivationSourceType {
	STATIC, STATIC_TEXT, EVERNOTE;

	companion object {
		const val STATIC_DISCRIMINATOR_NAME = "STATIC"
		const val STATIC_TEXT_DISCRIMINATOR_NAME = "STATIC_TEXT"
		const val EVERNOTE_DISCRIMINATOR_NAME = "EVERNOTE"
	}
}
