package akuchars.constraint

import java.util.HashMap

internal class IllegalImports {

	private val allIllegalImports = HashMap<String, List<String>>()

	val isEmpty: Boolean
		get() = allIllegalImports.keys.isEmpty()

	fun add(clazz: String, illegalImports: List<String>) {
		if (illegalImports.isEmpty()) {
			return
		}

		allIllegalImports[clazz] = illegalImports
	}

	fun count(): Int = allIllegalImports.keys.size

	override fun toString(): String {
		val sb = StringBuilder()
		allIllegalImports.keys.forEach { key ->
			sb.append("\n\n").append(key)
			allIllegalImports[key]?.forEach { value -> sb.append("\n\t").append(value) }
		}

		return sb.toString()
	}
}