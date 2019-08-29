package akuchars.infrastructure.spring

import akuchars.domain.user.repository.RoleRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.sql.DataSource


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
class AuthConfiguration(
		private val dataSource: DataSource,
		private val userRepository: RoleRepository
) : WebSecurityConfigurerAdapter() {

	@Bean
	fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

	override fun configure(http: HttpSecurity) {
		http.csrf().disable()
		http.headers().httpStrictTransportSecurity().disable()

		http.authorizeRequests()
				.antMatchers("/login.html", "/webjars/**", "/js/**", "/images/**", "/css/**", "/json/**")
				.permitAll()
				.anyRequest()
				.authenticated()
				.antMatchers("/api/v1/user/**")
				.hasAnyRole(* userRepository.findAll().map { it.role }.toTypedArray())
				.and()
				.formLogin()
				.loginPage("/login")
				.usernameParameter("user")
				.passwordParameter("password")
				.loginProcessingUrl("/authenticate")
				.successHandler {    //Success handler invoked after successful authentication
					_, res, auth ->
					for (authority in auth.authorities) {
						println(authority.authority)
					}
					println(auth.name)
					res.sendRedirect("/") // Redirect user to index/home page
				}.failureHandler { req, res, exp ->
					var errMsg = ""
					errMsg = if (exp::class.java.isAssignableFrom(BadCredentialsException::class.java)) {
						"Invalid username or password."
					} else {
						"Unknown error - " + exp.localizedMessage
					}
					req.session.setAttribute("message", errMsg)
					res.sendRedirect("/login")
				}
				.permitAll()
				.and()
				.logout()
				.permitAll()
				.and()
				.csrf()
				.disable();
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
				.usersByUsernameQuery("select email, password, enable from personal.users where email=?")
				.authoritiesByUsernameQuery(
						"select email, r.role " +
								"from personal.users " +
								"join personal.users_roles ur on ur.user_id = personal.users.id " +
								"join personal.roles r on ur.role_id = r.id " +
								"where email=?"
				)
				.passwordEncoder(passwordEncoder())
	}
}