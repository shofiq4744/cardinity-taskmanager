package com.cardinity.taskmanager.auth;

import java.util.Arrays;
import java.util.Objects;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private final UserRepository userRepository;

	public UserDetailsServiceImpl(UserRepository userRepository) {
		super();
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.findByUsername(username);
		if(Objects.isNull(user)) return null;
		
		return new org.springframework.security.core.userdetails.User(
		          user.getUsername(), user.getPassword(), user.isEnabled(), true, true, 
		          true, Arrays.asList(new SimpleGrantedAuthority(user.getRole().name())));
	}

}
