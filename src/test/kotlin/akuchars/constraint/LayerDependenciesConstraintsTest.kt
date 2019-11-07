package akuchars.constraint

import org.jboss.forge.roaster.Roaster
import org.jboss.forge.roaster.model.source.JavaSource
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

class LayerDependenciesConstraintsTest {
	/**
	 * Walidacja zależności między warstwami
	 *
	 */
	@ParameterizedTest(name = "({0}): {1} layer should not depend on {2} layer")
	@MethodSource("layersAndExcludes")
	@Throws(IOException::class)
	//todo przenieść ten test do osobnego modułu
	fun validateLayerDependencies(msg: String, fromLayer: String, toLayer: String, excludedClassessToAnalyze: List<Class<*>>, excludedImports: List<Class<*>>) {
		//given
		val skippedClasses = excludedClassessToAnalyze.map(Class<*>::getName)
		val illegalImports = IllegalImports()
		val excluded = excludedImports.map(Class<*>::getName)
		findAllSourceFilesWithPackage(fromLayer)
			.map(this::parse)
			.filter { src -> !skippedClasses.contains(src.getQualifiedName()) }
			.forEach { source -> illegalImports.add(source.getQualifiedName(), returnIllegalImports(source, toLayer, excluded)) }

		//then
		Assertions.assertTrue(illegalImports.isEmpty, "Found illegal dependencies / imports - " + msg + " (in " + illegalImports.count() + " classes) from '" +
			fromLayer + "' layer to '" + toLayer + "' layer: " + illegalImports.toString())
	}

	private fun parse(path: String): JavaSource<*> {
		try {
			return Roaster.parse(JavaSource::class.java, File(path))
		} catch (e: Exception) {
			throw IllegalStateException("For path: $path", e)
		}
	}

	@Throws(IOException::class)
	private fun findAllSourceFilesWithPackage(packageName: String, filePathEnding: Array<String> = arrayOf(".kt", ".java")): List<String> {
		return Files.walk(Paths.get(MAIN_JAVA_PREFIX))
			.map { filePath -> filePath.toAbsolutePath().toString() }
			.filter { filePath -> filePathEnding.any { filePath.endsWith(it) } }
			.filter { filePath -> filePath.contains(File.separator + packageName.replace(".", File.separator) + File.separator) }
			.filter { src -> !src.contains("package-info.java") }
			.toList()
	}

	private fun returnIllegalImports(source: JavaSource<*>, illegalPackage: String, excludedImports: List<String>): List<String> {
		return source.getImports()
			.map { it.qualifiedName }
			.filter { i -> i.contains(".$illegalPackage.") }
			.filter { i -> !excludedImports.contains(i) }
	}

	companion object {
		private val MAIN_JAVA_PREFIX = "." + File.separator + "src" + File.separator + "main" + File.separator + "kotlin" + File.separator

		@JvmStatic
		private fun layersAndExcludes(): List<Arguments> {
			return listOf(
				Arguments.of("DOM nie może zawierać APP", "domain", "application", listOf<Any>(), listOf<Any>(),
					Arguments.of("DOM nie może zawierać UI", "domain", "ui", listOf<Any>(), listOf<Any>()),
					Arguments.of("DOM nie może zawierać INF", "domain", "infrastructure", listOf<Any>(), listOf<Any>())
				),
				Arguments.of("APP nie może zwierać UI", "application", "ui", listOf<Any>(), listOf<Any>()),
				Arguments.of("APP nie może zwierać INF", "application", "infrastructure", listOf<Any>(), listOf<Any>()),
				Arguments.of("UI nie może zawierać DOM", "ui", "domain", listOf<Any>(), listOf<Any>(Page::class.java, PageRequest::class.java)),
				Arguments.of("INF nie może zawierać UI", "infrastructure", "ui", listOf<Any>(), listOf<Any>())
			)
		}
	}
}