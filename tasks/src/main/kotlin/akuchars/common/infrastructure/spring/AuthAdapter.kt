package akuchars.common.infrastructure.spring

import akuchars.user.application.query.UserQueryService
import akuchars.user.domain.repository.RoleRepository
import akuchars.user.domain.repository.UserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.sql.DataSource

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
class AuthConfig {
	@Bean
	fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()
}

@Configuration
class AuthAdapter(
		private val dataSource: DataSource,
		private val roleRepository: RoleRepository,
		private val passwordEncoder: PasswordEncoder,
		private val userQueryService: UserQueryService,
		private val userRepository: UserRepository

) : WebSecurityConfigurerAdapter() {

	override fun configure(http: HttpSecurity) {
		http.csrf().disable()
		http.headers().httpStrictTransportSecurity().disable()

		http.authorizeRequests()
				.antMatchers("/login.html", "/webjars/**", "/js/**", "/images/**", "/css/**", "/json/**")
				.permitAll()
				.anyRequest()
				.authenticated()
				.antMatchers("/api/v1/user/**")
				.hasAnyRole(* roleRepository.findAll().map { it.role }.toTypedArray())
				.and()
				.formLogin()
				.loginPage("/login")
				.usernameParameter("user")
				.passwordParameter("password")
				.loginProcessingUrl("/authenticate")
				.successHandler(handleSuccess())
				.failureHandler(handleFailure())
				.permitAll()
				.and()
				.logout()
				.permitAll()
				.and()
				.csrf()
				.disable();
	}

	override fun configure(auth: AuthenticationManagerBuilder) {
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
				.passwordEncoder(passwordEncoder)
	}

	private fun handleFailure(): (HttpServletRequest, HttpServletResponse, AuthenticationException) -> Unit {
		return { req, res, exp ->
			val errMsg = if (exp::class.java.isAssignableFrom(BadCredentialsException::class.java)) {
				"Invalid username or password."
			} else {
				"Unknown error - " + exp.localizedMessage
			}
			req.session.setAttribute("message", errMsg)
			res.sendRedirect("/login")
		}
	}

	private fun handleSuccess(): (HttpServletRequest, HttpServletResponse, Authentication) -> Unit {
		return { _, res, auth ->
			for (authority in auth.authorities) {
				println(authority.authority)
			}
			println(auth.name)
			res.sendRedirect("/")
			SecurityContextUserHolder.loggedUser = userQueryService.getLoggedUser().let {
				userRepository.findById(it.id).get()
			}
		}
	}
}