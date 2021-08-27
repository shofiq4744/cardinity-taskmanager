package com.cardinity.taskmanager.auth;

import java.util.Set;

import lombok.Data;

@Data
public class JwtResponse {
	private String accessToken;
	private String refreshToken;
	private String type = "Bearer";
	private String username;
	private String name;

	Set<String> authorities;

	public JwtResponse(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}

}
