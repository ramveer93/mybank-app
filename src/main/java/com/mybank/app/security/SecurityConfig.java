package com.mybank.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomUserDetailService customUserDetailService;
	
	@Autowired
	private JwtFilter jwtFilter;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailService);
	}
	
	@SuppressWarnings("deprecation")
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
		      //  .antMatchers("/v1/**").permitAll()
		       // .antMatchers("/api").permitAll()
				.antMatchers("/v1/authentication/token").permitAll()
//				.antMatchers("/v1/token").permitAll()
//				.antMatchers("/v1/addUser").permitAll()
//				.antMatchers("/css/**").permitAll()
//				.antMatchers("/js/**").permitAll()
//				.antMatchers("/favicon.ico").permitAll()
				 
				 //TODO : uncomment below line if you want to authenticate
				// .antMatchers("/v1/**").hasIpAddress(ip)
				.anyRequest().authenticated().and().exceptionHandling()
				.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                                   "/configuration/ui",
                                   "/swagger-resources/**",
                                   "/configuration/security",
                                   "/swagger-ui.html",
                                   "/webjars/**");
    }
}
