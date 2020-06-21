package com.example.web.config.oauth2;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.web.model.User;
import com.example.web.repository.UserRepository;
import com.example.web.util.OAuth2UserInfo;
import com.example.web.util.OAuth2UserInfoFactory;

@Service
public class CustomOAuth2UserDetailsService extends DefaultOAuth2UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("In oauth2 User details service");
		OAuth2User oAuth2User = super.loadUser(userRequest);
		OAuth2UserInfo oAuth2UserInfo= OAuth2UserInfoFactory.getOAuth2UserInfo(userRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
		
		System.out.println("In oidc User service");		
		User user = null; 
		user = userRepository.findByEmail(oAuth2UserInfo.getEmail());
		createOrUpdateUser(user, oAuth2UserInfo);
//		List<GrantedAuthority> authorities = getUserAuthorities(user.getUserRoles());
//		return new DefaultOidcUser(authorities, oidcUser.getIdToken());		
		return new User(oAuth2User.getAttributes(), oAuth2UserInfo.getEmail());
//		return oAuth2User;
	}
	
	private void createOrUpdateUser(User user, OAuth2UserInfo oAuth2UserInfo) {
		if (user == null) {
			user = new User();
			user.setFirstName(oAuth2UserInfo.getName());
			user.setEmail(oAuth2UserInfo.getEmail());
			user.setImagePath(oAuth2UserInfo.getImageUrl());
//			List<GrantedAuthority> authorities = getUserAuthorities(user.getUserRoles());
//			return new DefaultOidcUser(authorities, oidcUser.getIdToken());
		}else {
			user.setFirstName(oAuth2UserInfo.getName());
			user.setImagePath(oAuth2UserInfo.getImageUrl());
		}
		userRepository.save(user);
		System.out.println("User : "+ user.toString());
	}

}
