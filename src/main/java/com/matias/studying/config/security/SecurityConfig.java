package com.matias.studying.config.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.matias.studying.config.security.common.Role;
import com.matias.studying.config.security.constants.Paths;
import com.matias.studying.config.security.filter.CustomAccessDeniedHandler;
import com.matias.studying.config.security.filter.CustomAuthenticationEntryPoint;
import com.matias.studying.config.security.filter.JwtRequestFilter;

@EnableWebSecurity
@Configuration
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	AccessDeniedHandler accessDeniedHandler() {
		return new CustomAccessDeniedHandler();
	}

	@Bean
	AuthenticationEntryPoint authenticationEntryPoint() {
		return new CustomAuthenticationEntryPoint();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	public void configure(AuthenticationManagerBuilder managerBuilder) throws Exception {
		managerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).cors(cors -> {
			cors.configurationSource(request -> {
				CorsConfiguration config = new CorsConfiguration();
				// Configuración de CORS. Permite todos los origins.
				config.setAllowedOrigins(List.of("*"));
				config.setAllowedMethods(List.of("*"));
				config.setAllowedHeaders(List.of("*"));
				return config;
			});
		}).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeRequests(requests -> requests.antMatchers(Paths.DOCUMENTATION_PATHS).permitAll()
						.antMatchers(HttpMethod.POST, Paths.AUTH + Paths.REGISTER).permitAll()
						.antMatchers(HttpMethod.POST, Paths.AUTH + Paths.LOGIN).permitAll()
						.antMatchers(HttpMethod.GET, Paths.AUTH + Paths.ME)
						.hasAnyRole(Role.USER.name(), Role.ADMIN.name()).antMatchers(HttpMethod.GET, Paths.USERS)
						.hasRole(Role.ADMIN.name()).antMatchers(HttpMethod.DELETE, Paths.USERS + Paths.ID)
						.hasRole(Role.ADMIN.name()).antMatchers(HttpMethod.PATCH, Paths.USERS + Paths.ID)
						.hasAnyRole(Role.USER.name(), Role.ADMIN.name()).anyRequest().authenticated())
				.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling(handling -> handling.accessDeniedHandler(accessDeniedHandler())
						.authenticationEntryPoint(authenticationEntryPoint()));
	}

}