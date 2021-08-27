package com.cardinity.taskmanager.auth;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.cardinity.taskmanager.common.ApiController;
import com.cardinity.taskmanager.utill.JwtTokenUtil;

@ApiController
public class AuthController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	private final AuthenticationManager authenticationManager;
	
	private final UserRepository userRepository;
	
	private final UserDetailsService userService;

    private final JwtTokenUtil tokenProvider;
	
	public AuthController(AuthenticationManager authenticationManager,
						  UserRepository userRepository,
						  UserDetailsService userService,
						  JwtTokenUtil tokenProvider) {
		this.authenticationManager = authenticationManager;
		this.userRepository = userRepository;
		this.userService = userService;
		this.tokenProvider = tokenProvider;
	}
	
	
	@PostMapping(path="/authenticate")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		User user = userRepository.findByUsername(loginRequest.getEmail());
		LOGGER.info("INFO:: Login request for ", loginRequest.getEmail());
		if (Objects.nonNull(user) && user.isEnabled()) {
			try {
				Authentication authentication = authenticationManager
						.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				final UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());
				final String accessToken = tokenProvider.generateAccessToken(userDetails);
				final String refreshToken = tokenProvider.generateRefreshToken(loginRequest.getEmail());

				Set<String> authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
						.collect(Collectors.toSet());
				
				LOGGER.info("INFO:: Login valid for ", loginRequest.getEmail());
				JwtResponse jwtResponse = new JwtResponse(accessToken, refreshToken);
				jwtResponse.setAuthorities(authorities);
				jwtResponse.setUsername(user.getUsername());
				return ResponseEntity.ok(jwtResponse);
				
			} catch (Exception e) {
				return new ResponseEntity<String>(e.getCause().toString(), HttpStatus.UNAUTHORIZED);
			}
		} else {
			return new ResponseEntity<String>("User not found", HttpStatus.NOT_FOUND);
		}
	}

}
