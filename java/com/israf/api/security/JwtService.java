package com.israf.api.security;



import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.israf.api.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService { 

	@Value("${jwt.secret}")
    private String secretKey;
	
	public String generateToken(User user) {
		return Jwts.builder()
				.setSubject(user.getUsername()) 
				.claim("userId", user.getId()) 
				.claim("email", user.getEmail())
				.claim("username", user.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
	            .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30))
	            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
				.compact();
	}
	
	public String extractUsername(String token) {
		return Jwts.parserBuilder()
					.setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
					.build()
					.parseClaimsJws(token)
					.getBody()
					.getSubject();
	}
	
	public boolean validateToken(String token, String userId) {
		final String tokenUserId = extractUsername(token);
		return (tokenUserId.equals(userId) && !isTokenExpired(token));
	}
	
	private boolean isTokenExpired(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getExpiration()
				.before(new java.util.Date());
	}
	
	
}