package akuchars.infrastructure.spring

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.thymeleaf.spring5.view.ThymeleafViewResolver
import org.springframework.web.servlet.ViewResolver
import org.springframework.context.annotation.Bean
import org.thymeleaf.spring5.SpringTemplateEngine
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.thymeleaf.templateresolver.ITemplateResolver


@Configuration
class ThymeleafResolverConfiguration(@Value("\${spring.thymeleaf.template.resolver.prefix}")val  prefix: String) {

	@Bean
	fun templateResolver(): ClassLoaderTemplateResolver {

		val templateResolver = ClassLoaderTemplateResolver()

		templateResolver.prefix = prefix
		templateResolver.isCacheable = false
		templateResolver.suffix = ".html"
		templateResolver.setTemplateMode("HTML5")
		templateResolver.characterEncoding = "UTF-8"

		return templateResolver
	}

	@Bean
	fun templateEngine(templateResolver: ITemplateResolver): SpringTemplateEngine {

		val templateEngine = SpringTemplateEngine()
		templateEngine.setTemplateResolver(templateResolver)

		return templateEngine
	}

	@Bean
	fun viewResolver(templateResolver: ITemplateResolver): ViewResolver {

		val viewResolver = ThymeleafViewResolver()

		viewResolver.templateEngine = templateEngine(templateResolver)
		viewResolver.characterEncoding = "UTF-8"

		return viewResolver
	}
}