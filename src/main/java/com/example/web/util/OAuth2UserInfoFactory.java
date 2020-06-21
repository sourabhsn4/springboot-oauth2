package com.example.web.util;

import java.util.Map;

import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;

import com.example.web.exceptions.OAuth2LoginException;

public class OAuth2UserInfoFactory {
	public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(CommonOAuth2Provider.GOOGLE.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(CommonOAuth2Provider.FACEBOOK.toString())) {
            return new FaceBookOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2LoginException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
