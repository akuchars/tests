package akuchars.infrastructure.spring

import com.mongodb.MongoClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate

@Configuration
class MongoConfiguration {

	@Bean
	fun mongo(
			@Value("\${spring.data.mongodb.host}") host: String,
			@Value("\${spring.data.mongodb.port}") port: String
	): MongoClient = MongoClient("$host:$port")

	@Bean
	fun mongoTemplate(
			@Autowired mongo: MongoClient,
			@Value("\${spring.data.mongodb.database}") databaseName: String
	): MongoTemplate =
			MongoTemplate(mongo, databaseName)
}