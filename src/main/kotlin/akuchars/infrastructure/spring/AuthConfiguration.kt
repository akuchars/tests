package akuchars.infrastructure.spring

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.sql.DataSource

@Configuration
@EnableWebSecurity
class AuthConfiguration(
		private val dataSource: DataSource
) : WebSecurityConfigurerAdapter() {

	@Bean
	fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

	override fun configure(http: HttpSecurity) {
		http.csrf().disable()
				.authorizeRequests()
				.antMatchers("/", "/home").permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginPage("/login")
				.usernameParameter("user")
				.passwordParameter("password")
				.loginProcessingUrl("/authenticate")
				.permitAll()
				.and()
				.logout()
				.permitAll();
	}

	override fun configure(auth: AuthenticationManagerBuilder) {
//		auth.inMemoryAuthentication()
//				.withUser("user")
//				.password("password")
//				.password("USER")
//				.and()
//				.withUser("admin")
//				.password("admin")
//				.roles("USER", "ADMIN")

		auth.jdbcAuthentication()
				.dataSource(dataSource)
				.usersByUsernameQuery("select email, password from personal.users where email=?")
				.authoritiesByUsernameQuery(
						"select email, r.role " +
								"from personal.users" +
								"join personal.users_roles ur on ur.user_id = personal.users.id" +
								"join personal.roles r on ur.role_id = r.id" +
								"where email=?"
				)
				.passwordEncoder(passwordEncoder())
	}
}