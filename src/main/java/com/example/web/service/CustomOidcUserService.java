package com.example.web.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.example.web.model.User;
import com.example.web.repository.UserRepository;
import com.example.web.util.OAuth2UserInfo;
import com.example.web.util.OAuth2UserInfoFactory;

@Service
public class CustomOidcUserService extends OidcUserService{
//extends OidcUserService {
	
	@Autowired 
	private UserRepository userRepository;

//	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		System.out.println("Entered Oidc User service");
		OidcUser oidcUser = super.loadUser(userRequest);
		OAuth2UserInfo oAuth2User= OAuth2UserInfoFactory.getOAuth2UserInfo(userRequest.getClientRegistration().getRegistrationId(), oidcUser.getAttributes());
		
		System.out.println("In oidc User service");		
		User user = null; 
		user = userRepository.findByEmail(oidcUser.getEmail());
		createOrUpdateUser(user, oAuth2User);
//		List<GrantedAuthority> authorities = getUserAuthorities(user.getUserRoles());
//		return new DefaultOidcUser(authorities, oidcUser.getIdToken());		
		return new DefaultOidcUser(new ArrayList<GrantedAuthority>(), oidcUser.getIdToken());
	}
	
	private void createOrUpdateUser(User user, OAuth2UserInfo oAuth2User) {
		if (user == null) {
			user = new User();
			user.setFirstName(oAuth2User.getName());
			user.setEmail(oAuth2User.getEmail());
			user.setImagePath(oAuth2User.getImageUrl());
//			List<GrantedAuthority> authorities = getUserAuthorities(user.getUserRoles());
//			return new DefaultOidcUser(authorities, oidcUser.getIdToken());
		}else {
			user.setFirstName(oAuth2User.getName());
			user.setImagePath(oAuth2User.getImageUrl());
		}
		userRepository.save(user);
	}
	/*
	 * private List<GrantedAuthority> getUserAuthorities(Collection<UserRoles>
	 * userRoles) { Set<GrantedAuthority> roles = new HashSet<>();
	 * userRoles.forEach((role) -> roles.add(new
	 * SimpleGrantedAuthority(role.getRoleName()))); return new
	 * ArrayList<GrantedAuthority>(roles); }
	 */
}
