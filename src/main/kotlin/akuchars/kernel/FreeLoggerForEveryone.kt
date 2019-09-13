package akuchars.kernel

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

val allLoggersMap = ConcurrentHashMap<KClass<*>, Logger>()

inline val <reified T: Any> KClass<T>.logger: Logger
	get() = allLoggersMap.getOrPut(T::class, { LoggerFactory.getLogger(T::class.java) })

inline val <reified T: Any> T.logger: Logger
	get() = allLoggersMap.getOrPut(T::class, { LoggerFactory.getLogger(T::class.java) })