package akuchars.domain.common

abstract class DomainException(val frontErrorCode: String, val msg: String) : Exception(msg) {
	open fun getMessageResolverParams(): Array<*>? = null
}