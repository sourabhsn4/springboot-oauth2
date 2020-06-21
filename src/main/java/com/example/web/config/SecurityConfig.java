package com.example.web.config;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import com.example.web.config.oauth2.CustomOAuth2UserDetailsService;
import com.example.web.config.oauth2.OAuth2AuthenticationFailureHandler;
import com.example.web.config.oauth2.OAuth2AuthenticationSuccessHandler;
import com.example.web.service.CustomOidcUserService;
import com.example.web.service.TokenService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true,jsr250Enabled = true, securedEnabled = true)
//@PropertySource({ "classpath:application-oauth2.properties" })
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
	
	@Autowired
	OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
	
	@Autowired
	CustomOidcUserService oidcUserService;
	
	@Autowired
	TokenService tokenService;
	
	@Autowired
	Environment env;
	
	@Autowired 
	CustomOAuth2UserDetailsService oAuth2UserService;
	
//	@Bean
//	@Override
//	protected AuthenticationManager authenticationManager() throws Exception {
//		return super.authenticationManager();
//	}

	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		super.configure(auth);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable()
				.authorizeRequests()
				.antMatchers("/oauth2/authorize-client/google", "/oauth2/authorize-client/facebook").permitAll()
				.antMatchers("/call/mapping","/search","/afterLogin", "/uploadfile").permitAll()
				.anyRequest().authenticated()
				.and()
					.oauth2Login()
					.authorizationEndpoint()
						.baseUri("/oauth2/authorize-client")
//						.authorizationRequestRepository(authorizationRequestRepository())
				.and()
					.redirectionEndpoint()
					.baseUri("/oauth2/callback/*")
					.and()
					.userInfoEndpoint()
				.and()	
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)	
                	.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Bean
	public AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
		return new HttpSessionOAuth2AuthorizationRequestRepository();
	}

	@Bean
	public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
		DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient = new DefaultAuthorizationCodeTokenResponseClient();
		return accessTokenResponseClient;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/profile/pic/**");
	}
	
	// additional configuration for non-Spring Boot projects
	private static List<String> clients = Arrays.asList("google", "facebook");

	// @Bean
	public ClientRegistrationRepository clientRegistrationRepository() {
		List<ClientRegistration> registrations = clients.stream().map(c -> getRegistration(c))
				.filter(registration -> registration != null).collect(Collectors.toList());
		return new InMemoryClientRegistrationRepository(registrations);
	}

	private static String CLIENT_PROPERTY_KEY = "spring.security.oauth2.client.registration.";

	private ClientRegistration getRegistration(String client) {
		String clientId = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-id");

		if (clientId == null) {
			return null;
		}

		String clientSecret = env.getProperty(CLIENT_PROPERTY_KEY + client + ".client-secret");
		if (client.equals("google")) {
			return CommonOAuth2Provider.GOOGLE.getBuilder(client).clientId(clientId).clientSecret(clientSecret).build();
		}
		if (client.equals("facebook")) {
			return CommonOAuth2Provider.FACEBOOK.getBuilder(client).clientId(clientId).clientSecret(clientSecret)
					.build();
		}
		return null;
	}
}
