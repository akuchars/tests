package akuchars.infrastructure.spring

import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import java.util.Locale
import javax.servlet.http.HttpServletRequest

class LocaleResolverImpl: AcceptHeaderLocaleResolver() {

	override fun resolveLocale(request: HttpServletRequest): Locale {
		val locales = mutableListOf(
				Locale("en"),
				Locale("pl")
		)
		val headerLang = request.getHeader("Accept-Language")
		val locale = if (headerLang == null || headerLang.isEmpty()) Locale.getDefault()
		else Locale.lookup(Locale.LanguageRange.parse(headerLang), locales)
		LocaleContextHolder.setLocale(locale)
		return locale
	}
}