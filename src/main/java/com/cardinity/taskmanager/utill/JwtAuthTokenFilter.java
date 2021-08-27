package com.cardinity.taskmanager.utill;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

public class JwtAuthTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	private static final Logger logger = LoggerFactory.getLogger(JwtAuthTokenFilter.class);

	public Authentication getAuthentication(String token) {
		Map<String, Object> parseInfo = jwtTokenUtil.getUserParseInfo(token);
		List<String> rs = (List) parseInfo.get("role");
		Collection<GrantedAuthority> tmp = new ArrayList<>();
		for (String a : rs) {
			tmp.add(new SimpleGrantedAuthority(a));
		}
		UserDetails userDetails = User.builder().username(String.valueOf(parseInfo.get("username"))).authorities(tmp)
				.password("asd").build();
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());
		return usernamePasswordAuthenticationToken;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {			
			String requestTokenHeader = request.getHeader("Authorization");
			if (requestTokenHeader != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				logger.info("tokenHeader: " + requestTokenHeader);
				String username = null;
				String jwtToken = null;
				if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
					jwtToken = requestTokenHeader.substring(7);
					logger.info("token in requestfilter: " + jwtToken);
	
					try {
						username = jwtTokenUtil.getUsernameFromToken(jwtToken);
					} catch (IllegalArgumentException e) {
						logger.warn("Unable to get JWT Token");
					} catch (ExpiredJwtException e) {
					}
				} else {
					logger.warn("JWT Token does not begin with Bearer String");
				}
				if (username == null) {
					logger.info("token maybe expired: username is null.");
				} else {
					// DB access
					Authentication authen = getAuthentication(jwtToken);
					SecurityContextHolder.getContext().setAuthentication(authen);
				}
			}

		} catch (Exception e) {
			logger.error("Can NOT set user authentication -> Message: {}", e);
		}

		filterChain.doFilter(request, response);
	}

}
