package akuchars.infrastructure.spring

import org.springframework.context.annotation.Configuration
import org.thymeleaf.spring5.view.ThymeleafViewResolver
import org.springframework.web.servlet.ViewResolver
import org.springframework.context.annotation.Bean
import org.thymeleaf.spring5.SpringTemplateEngine
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver



@Configuration
class ThymeleafResolverConfiguration {

	@Bean
	fun templateResolver(): ClassLoaderTemplateResolver {

		val templateResolver = ClassLoaderTemplateResolver()

		templateResolver.prefix = "mytemplates/"
		templateResolver.isCacheable = false
		templateResolver.suffix = ".html"
		templateResolver.setTemplateMode("HTML5")
		templateResolver.characterEncoding = "UTF-8"

		return templateResolver
	}

	@Bean
	fun templateEngine(): SpringTemplateEngine {

		val templateEngine = SpringTemplateEngine()
		templateEngine.setTemplateResolver(templateResolver())

		return templateEngine
	}

	@Bean
	fun viewResolver(): ViewResolver {

		val viewResolver = ThymeleafViewResolver()

		viewResolver.templateEngine = templateEngine()
		viewResolver.characterEncoding = "UTF-8"

		return viewResolver
	}
}