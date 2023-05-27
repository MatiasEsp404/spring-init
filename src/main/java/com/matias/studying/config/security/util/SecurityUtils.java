package com.matias.studying.config.security.util;

import com.matias.studying.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.matias.studying.config.security.common.Role;

@Component
public class SecurityUtils {

	@Autowired
	private IUserRepository userRepository;

	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	public UserDetails getUserAuthenticated() {
		Object principal = getAuthentication().getPrincipal();
		String username = (principal instanceof UserDetails) ? ((UserDetails) principal).getUsername()
				: principal.toString();

		return userRepository.findByEmail(username);
	}

	public boolean hasAdminRole() {
		return getAuthentication().getAuthorities().stream()
				.anyMatch(grantedAuthority -> Role.ADMIN.getFullRoleName().equals(grantedAuthority.getAuthority()));
	}

}
