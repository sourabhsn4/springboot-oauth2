package com.example.web.config.oauth2;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.web.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	@Autowired
	private TokenService tokenService;

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	private static String clientUri = "/afterLogin";	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		System.out.println("In oauth2success handler");						
		String token = tokenService.constructJwtToken(authentication);
		if (response.isCommitted()) {
            System.out.println("Response has already been committed. Unable to redirect to " + clientUri);
            return;
        }

		String targetUrl = UriComponentsBuilder.fromUriString(clientUri)
                .queryParam("token", token)
                .build().toUriString();
		System.out.println("targetUrl: "+ targetUrl);
        //getRedirectStrategy().sendRedirect(request, response, targetUrl);		
		redirectStrategy.sendRedirect(request, response, targetUrl);
		//response.sendRedirect(targetUrl);
	}
}
