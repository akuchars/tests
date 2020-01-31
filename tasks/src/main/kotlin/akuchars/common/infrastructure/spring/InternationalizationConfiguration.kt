package akuchars.common.infrastructure.spring

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor


@Configuration
class InternationalizationConfiguration : WebMvcConfigurer {

	@Bean
	fun localeResolver(): LocaleResolver = LocaleResolverImpl()

	@Bean
	fun localeChangeInterceptor(): LocaleChangeInterceptor {
		return LocaleChangeInterceptor().apply {
			paramName = "lang"
		}
	}

	@Bean(name = ["messageSource"])
	fun bundleMessageSource(): ResourceBundleMessageSource {
		return ResourceBundleMessageSource().apply {
			setBasename("messages")
			setDefaultEncoding("UTF-8")
		}
	}

	override fun addInterceptors(registry: InterceptorRegistry) {
		registry.addInterceptor(localeChangeInterceptor())
	}
}