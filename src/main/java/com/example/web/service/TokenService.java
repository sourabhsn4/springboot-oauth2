package com.example.web.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.web.config.oauth2.SecurityConstants;
import com.example.web.model.User;
import com.example.web.repository.UserRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {
	
	@Autowired
	private UserRepository userRepository;
	
	public String constructJwtToken(Authentication authentication) {
		System.out.println("Principal: "+ authentication.getPrincipal());
		
		String login = null;
		if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.User) {
			org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
			login = user.getUsername();
		}else {
			User user = (User) authentication.getPrincipal(); 
			login = user.getEmail();
			System.out.println("Name property from oAuth2User : "+ user.getName());
		}

		User user = userRepository.findByEmail(login);
        if (login != null && login.length() > 0) {
            Claims claims = Jwts.claims().setSubject(login);
//            List<String> roles = new ArrayList<>();
//            user.getAuthorities().stream().forEach(authority -> roles.add(authority.getAuthority()));
//	          List<String> roles = user.getUserRoles().stream().map(UserRoles::getRoleName).collect(Collectors.toList());
	                      
//            claims.put("roles", roles);
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                    .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                    .compact();
            return token;
        }  
        return null;
	}
}
