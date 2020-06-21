package com.example.web.model;

import java.util.Collection;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Entity
@Table(name = "user")
public class User implements OAuth2User, UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "first_name", columnDefinition = "char(50)")
	private String firstName;
	
	@Column(name = "middle_name", columnDefinition = "char(50)")
	private String middleName;
	
	@Column(name = "last_name", columnDefinition = "char(50)")
	private String lastName;
	
	@Column(name = "email", columnDefinition = "char(50)")
	private String email;
	
	@Column(name = "image_path")
	private String imagePath;
		
	@Transient 
	private Map<String,Object> attributes;
	
	public User(){
		
	}
	
	public User(Map<String, Object> attributes, String email){
		this.attributes = attributes;
		this.email = email;
	}
	
	public User setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}
	
	public User setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}
	
	public User setMiddleName(String middleName) {
		this.middleName = middleName;
		return this;
	}
	
	public User setEmail(String email) {
		this.email = email;
		return this;
	}
	
	public User setImagePath(String imagePath) {
		this.imagePath = imagePath;
		return this;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getMiddleName() {
		return middleName;
	}

	public String getEmail() {
		return email;
	}
	
	public String getImagePath() {
		return imagePath;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", middleName=" + middleName + ", lastName=" + lastName
				+ "]";
	}

	@Override
	public Map<String, Object> getAttributes() {		
		//return this.attributes;
		return null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return this.email;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return this.email;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}
}
