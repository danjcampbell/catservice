package com.danjcampbell.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.danjcampbell.security.CatServiceUser.Role;

@Component
public class UserDetailsServiceImp implements UserDetailsService {

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//This is just an in memory store of 2 different credentials
		//In a production environment I would store this in a db
		
		CatServiceUser user = new CatServiceUser();
		if (username.equalsIgnoreCase("admin")) {
			user.setUsername("admin");
			user.setPassword(passwordEncoder.encode("admin"));
			user.grantAuthority(Role.ROLE_ADMIN);
		} else if (username.equalsIgnoreCase("user")) {
			user.setUsername("user");
			user.setPassword(passwordEncoder.encode("user"));
			user.grantAuthority(Role.ROLE_USER);
		}
		return user;
	}
}