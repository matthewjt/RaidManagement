package com.tomasov.raidmanagement.security;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.tomasov.raidmanagement.user.RaidManagementRole;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{

	private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

	@Autowired
	private UserDatabaseAuthenticationProvider authenticationProvider;

	@Autowired
	private CurrentUserDetailsService userDetailsService;

	@Configuration
	@Order(1)
	public static class ApiWebSecurityConfig extends WebSecurityConfigurerAdapter
	{

		@Override
		protected void configure(HttpSecurity http) throws Exception
		{
			http.csrf().requireCsrfProtectionMatcher(new RequestMatcher()
			{
				private Pattern allowedMethods = Pattern.compile("^(GET|HEAD|TRACE|OPTIONS)$");
				// force CSRF for everything in /secure and /login.
				// private RegexRequestMatcher apiMatcher = new
				// RegexRequestMatcher("[.*][/secure]{0,1}[/login]{0,1}[.*]",
				// null);
				// force CSRF for everything EXCEPT /rs/** and /stream/**
				private RegexRequestMatcher apiMatcher = new RegexRequestMatcher(".*^(/rs|/stream).*", null);

				@Override
				public boolean matches(HttpServletRequest request)
				{
					// No CSRF due to allowedMethod
					if (allowedMethods.matcher(request.getMethod()).matches())
					{
						logger.debug("NOT CSRF Required for matching pattern for csrf: {}", request.getRequestURI());
						return false;
					}
					// No CSRF due to api call
					if (apiMatcher.matches(request))
					{
						logger.debug("NOT CSRF Required for matching pattern for csrf: {}", request.getRequestURI());
						return false;
					}

					// CSRF for everything else that is not an API call or an
					// allowedMethod
					logger.debug("CSRF Required for matching pattern for csrf: {}", request.getRequestURI());
					return true;
				}
			});

			// @formatter:off
			http
					.requestMatchers().antMatchers("/rs/**", "/stream/**")
					.and().authorizeRequests()
									.antMatchers("/rs/config/**").hasAuthority(RaidManagementRole.ADMIN.toString())
									.antMatchers("/rs/admin/**").hasAuthority(RaidManagementRole.ADMIN.toString())
									.antMatchers("/rs/status/**").hasAuthority(RaidManagementRole.ADMIN.toString())
									.antMatchers("/rs/library/**").authenticated()
									.antMatchers("/rs/user/**").authenticated()
									.antMatchers("/stream/**").authenticated()
									.anyRequest().denyAll()
					.and().httpBasic()
//							.and().csrf().disable()
					;
			// @formatter:on
		}

	}

	@Configuration
	@Order(2)
	public static class FormWebSecurityConfig extends WebSecurityConfigurerAdapter
	{

		@Override
		public void configure(WebSecurity web) throws Exception
		{
			web.ignoring().antMatchers("/static/css/**", "/static/js/**", "/img/**", "/lib/**");
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception
		{
			http.sessionManagement()
	        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
			
			// @formatter:off
			http
					.authorizeRequests()
									.antMatchers("/secure/admin/**").hasAnyAuthority(RaidManagementRole.ADMIN.getRoleName(), RaidManagementRole.GUILDMASTER.getRoleName())
									.antMatchers("/secure/**").authenticated()
									.antMatchers("/", "/index").permitAll()
									.antMatchers("/browse").permitAll()
									.anyRequest().permitAll()
					.and().formLogin()
									.loginPage("/login").permitAll()
									.defaultSuccessUrl("/browse", true)
									.failureUrl("/login?error")
					.and().logout()
									.logoutRequestMatcher(new AntPathRequestMatcher( "/logout"))
									.deleteCookies("remember-me")
									.logoutSuccessUrl("/login?logout")
									.permitAll()
					.and().rememberMe();
			// @formatter:on
		}
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.eraseCredentials(false);
		auth.authenticationProvider(authenticationProvider).userDetailsService(userDetailsService);
	}
	
	@SuppressWarnings("deprecation")
	@Bean
	public static NoOpPasswordEncoder passwordEncoder() {
	return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance();
	}
}
