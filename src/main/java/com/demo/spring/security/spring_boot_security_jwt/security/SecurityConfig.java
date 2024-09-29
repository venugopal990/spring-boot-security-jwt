package com.demo.spring.security.spring_boot_security_jwt.security;

import static org.springframework.security.config.Customizer.withDefaults;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.demo.spring.security.spring_boot_security_jwt.jwt.AuthEntryPointJwt;
import com.demo.spring.security.spring_boot_security_jwt.jwt.AuthTokenFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Autowired
	DataSource dataSource;
	
	@Autowired
	AuthEntryPointJwt unauthorizeHandler;
	
	
	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}
	
	@Bean
	public SecurityFilterChain securityChain(HttpSecurity http) throws Exception {
		
		http.authorizeHttpRequests(request -> request.requestMatchers("/h2-console/**").permitAll().requestMatchers("/signin").permitAll()
				.anyRequest().authenticated());//added new requestmatcher "/signin" for JWT 
		//http.formLogin(withDefaults());
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizeHandler));//JWT
		//http.httpBasic(withDefaults());//disabled for jwt
		http.csrf(csrf -> csrf.disable());
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);//JWT
		return http.build();
	}
	
	
	/*//commented for JWT
	 * @Bean
	public UserDetailsService userDetailService() {
		
		UserDetails user = User.withUsername("user").
				password(passwordEncoder().encode("userpassword")).
				roles("USER")
				.build();
		
		UserDetails admin = User.withUsername("admin")
				.password(passwordEncoder().encode("adminpassword"))
				.roles("ADMIN")
				.build();
		JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
		jdbcUserDetailsManager.createUser(user);
		jdbcUserDetailsManager.createUser(admin);
		return jdbcUserDetailsManager;
		
		//return new InMemoryUserDetailsManager(user,admin);
		
	}*/
	
	
		//jwt
	 	@Bean
	    public UserDetailsService userDetailsService(DataSource dataSource) {
	        return new JdbcUserDetailsManager(dataSource);
	    }

	 	//jwt
	    @Bean
	    public CommandLineRunner initData(UserDetailsService userDetailsService) {
	        return args -> {
	           // JdbcUserDetailsManager manager = (JdbcUserDetailsManager) userDetailsService;
	            UserDetails user1 = User.withUsername("user")
	                    .password(passwordEncoder().encode("userpassword"))
	                    .roles("USER")
	                    .build();
	            UserDetails admin = User.withUsername("admin")
	                    //.password(passwordEncoder().encode("adminPass"))
	                    .password(passwordEncoder().encode("adminpassword"))
	                    .roles("ADMIN")
	                    .build();

	            JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
	            userDetailsManager.createUser(user1);
	            userDetailsManager.createUser(admin);
	        };
	    }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
		return builder.getAuthenticationManager();
	}


}
