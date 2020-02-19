package akuchars.motivation.domain.model

enum class SourceType {
	DEFAULT, STATIC_TEXT, EVERNOTE, WWW;

	companion object {
		const val STATIC_DISCRIMINATOR_NAME = "STATIC"
		const val STATIC_TEXT_DISCRIMINATOR_NAME = "STATIC_TEXT"
		const val EVERNOTE_DISCRIMINATOR_NAME = "EVERNOTE"
	}
}
