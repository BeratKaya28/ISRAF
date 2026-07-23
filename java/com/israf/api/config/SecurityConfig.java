package com.israf.api.config;

import java.util.Arrays;


import org.springframework.context.annotation.Bean;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.israf.api.repository.UserRepository;
import com.israf.api.security.JwtAuthenticationFilter;
import com.israf.api.security.OAuth2SuccessHandler;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final UserRepository userRepository;
	private OAuth2SuccessHandler oAuth2SuccessHandler;
	
	public SecurityConfig(@Lazy JwtAuthenticationFilter jwtAuthFilter, UserRepository userRepository, OAuth2SuccessHandler oAuth2SuccessHandler) {
		this.jwtAuthenticationFilter = jwtAuthFilter;
		this.userRepository = userRepository;
		this.oAuth2SuccessHandler = oAuth2SuccessHandler;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		http
			.csrf(csrf -> csrf.disable())
			.cors(cors -> cors.configurationSource(corsConfigurationSource()))
			.exceptionHandling(exc -> exc
					.authenticationEntryPoint((request, response, authException) -> {
						if (request.getRequestURI().startsWith("/api/")) {
							response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
						} else {
							response.sendRedirect("/login.html");
						}
					})
				)
			.authorizeHttpRequests(auth -> auth
					.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
					.requestMatchers("/api/admin/**").hasRole("ADMIN")
					.requestMatchers(HttpMethod.GET, "/api/categories/**", "/api/store-types/**").permitAll()
					.requestMatchers("/", "/api/auth/**", "/*.html", "/*.css", "/*.js", "/images/**","/api/images/**", "/uploads/**", "/error").permitAll()
					.requestMatchers("/login/oauth2/**", "/oauth2/**").permitAll()
					.requestMatchers("/h2-console/**").permitAll()
					.anyRequest().authenticated()
					)
					.oauth2Login(oauth2 -> oauth2
							.loginPage("/login.html")
							.successHandler(oAuth2SuccessHandler)
							)
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS)	
					)
				.headers(headers -> headers.frameOptions(frame -> frame.disable()))
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(Arrays.asList("*")); 
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
		return configuration.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
