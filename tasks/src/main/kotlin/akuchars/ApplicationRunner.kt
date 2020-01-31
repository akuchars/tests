package akuchars

import akuchars.motivation.application.command.MotivationNotificationService
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.web.servlet.config.annotation.EnableWebMvc


@SpringBootApplication
@EnableWebMvc
@EnableJpaAuditing
@EnableJpaRepositories
class ApplicationRunner

fun main(args: Array<String>) {
	val app = runApplication<ApplicationRunner>(*args)

	app.getBean(MotivationNotificationService::class.java).sendMotivationToInterested()

}