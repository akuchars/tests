package akuchars.common.infrastructure.spring

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
import springfox.documentation.builders.PathSelectors.regex
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2


@Configuration
@EnableSwagger2
class SwaggerConfiguration : WebMvcConfigurationSupport() {

	fun api(): Docket {
		return Docket(DocumentationType.SWAGGER_2)
			.select()
			.apis(RequestHandlerSelectors.basePackage("akuchars"))
			.paths(regex("/api/*"))
			.build()
	}

	override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
		registry.apply {
			addResourceHandler("swagger-ui.html")
				.addResourceLocations("classpath:/META-INF/resources/")

			addResourceHandler("/webjars/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/")

		}
	}
}