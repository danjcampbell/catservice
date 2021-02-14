package com.codeacademy.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CatServiceUser implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String username;

	private String password;

	private boolean accountNonExpired;

	private boolean accountNonLocked;

	private boolean credentialsNonExpired;

	private boolean enabled;

	private List<Role> roles = new ArrayList<CatServiceUser.Role>();

	public CatServiceUser() {
		this.accountNonExpired = true;
		this.accountNonLocked = true;
		this.credentialsNonExpired = true;
		this.enabled = true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	public void grantAuthority(Role authority) {
		if (roles == null)
			roles = new ArrayList<>();
		roles.add(authority);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.toString())));
		return authorities;
	}

	public enum Role {
		ROLE_USER, ROLE_ADMIN
	}

	public void setRole(Role roleAdmin) {
		this.roles.add(roleAdmin);
	}
}