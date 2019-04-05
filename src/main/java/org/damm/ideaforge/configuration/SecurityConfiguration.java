package org.damm.ideaforge.configuration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableOAuth2Sso
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private DataSource dataSource;

	@Value("${spring.queries.users-query}")
	private String usersQuery;

	@Value("${spring.queries.roles-query}")
	private String rolesQuery;

	@Bean
	public JdbcTemplate JdbcTemplate() {
		return new JdbcTemplate(dataSource);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// @formatter:off
		auth.jdbcAuthentication()
			.usersByUsernameQuery(usersQuery)
			.authoritiesByUsernameQuery(rolesQuery)
			.dataSource(dataSource)
			.passwordEncoder(bCryptPasswordEncoder);
		// @formatter:on
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http.antMatcher("/**")
			.authorizeRequests()
			.antMatchers("/", "/login**")
			.permitAll()
			.anyRequest()
			.authenticated();
		// @formatter:on
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
	}

//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		// @formatter:off
//		http.authorizeRequests()
//			.antMatchers("/login").permitAll()
//			.antMatchers("/registration").permitAll()
//			.antMatchers("/**")
//				.hasAuthority("USER")
//				.anyRequest()
//				.authenticated()
//				.and()
//				.csrf()
//				.disable()
//				.formLogin()
//				.loginPage("/login")
//				.failureUrl("/login?error=true")
//				.defaultSuccessUrl("/")
//				.usernameParameter("email")
//				.passwordParameter("password")
//				.and()
//				.logout()
//				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//				.logoutSuccessUrl("/")
//				.and()
//				.exceptionHandling()
//				.accessDeniedPage("/access-denied");
//		// @formatter:on
//	}

}
