package br.com.grillo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.grillo.config.jwt.AuthTokenFilter;
import br.com.grillo.exception.CustomAccessDeniedHandler;
import br.com.grillo.exception.CustomAccessUnauthorizeddHandler;
import br.com.grillo.service.UserService;

// @Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserService userService;

	@Autowired
	private CustomAccessUnauthorizeddHandler unauthorizedHandler;

	@Autowired
	private CustomAccessDeniedHandler accessDeniedHandler;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors()
			.and()
			.csrf().disable()
			.exceptionHandling()
			.accessDeniedHandler(accessDeniedHandler)
			.and()
        	.exceptionHandling()
			.authenticationEntryPoint(unauthorizedHandler)
			.and()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and().authorizeRequests()
			.antMatchers("/actuator/**")
				.permitAll()
			.antMatchers("/api/auth/**")
				.permitAll()
			.antMatchers(HttpMethod.POST, "/categories", "/products", "/partners", "/finances").hasRole("ADMIN")
			.antMatchers(HttpMethod.GET, "/categories/**", "/products/**", "/partners/**", "/finances/**").hasAnyRole("USER", "ADMIN")
			.anyRequest()
				.authenticated()
			.and()
				.logout()
				.deleteCookies()
				.invalidateHttpSession(true);

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}
}