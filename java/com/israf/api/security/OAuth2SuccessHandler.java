package com.israf.api.security;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.israf.api.model.Role;
import com.israf.api.model.User;
import com.israf.api.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler{

	private JwtService jwtService;
	private UserRepository userRepository;
	
	public OAuth2SuccessHandler(JwtService jwtService, UserRepository userRepository) {
		this.jwtService = jwtService;
		this.userRepository = userRepository;
	}
	
	@Override
	@Transactional
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException{
		
		OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
		String email = oAuth2User.getAttribute("email");
		
		User user = userRepository.findByEmail(email).orElseGet(() -> {
			User newUser = new User();
			newUser.setEmail(email);
			newUser.setUsername(email);
			newUser.setFirstName(oAuth2User.getAttribute("given_name"));
			newUser.setLastName(oAuth2User.getAttribute("family_name"));
			newUser.setRole(Role.SELLER);
			return userRepository.save(newUser);
		});
		
		String token = jwtService.generateToken(user);
		
		String baseUrl = request.getScheme() + "://" + request.getServerName();
	    if (request.getServerPort() != 80 && request.getServerPort() != 443) {
	        baseUrl += ":" + request.getServerPort();
	    }
		
		String targetUrl;
		
		if (user.getStores() == null || user.getStores().isEmpty()) {
	        targetUrl = baseUrl + "/register.html?token=" + token + "&email=" + email;
	    } else {
	        targetUrl = baseUrl + "/sellerPage.html?token=" + token;
	    }
		
		getRedirectStrategy().sendRedirect(request, response, targetUrl);
	}
	
	
	
}
