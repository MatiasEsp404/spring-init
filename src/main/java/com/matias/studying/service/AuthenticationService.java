package com.matias.studying.service;

import com.matias.studying.config.exception.runtime.EntityNotFoundException;
import com.matias.studying.config.exception.runtime.InvalidCredentialsException;
import com.matias.studying.config.exception.runtime.UserAlreadyExistException;
import com.matias.studying.config.security.common.Role;
import com.matias.studying.config.security.util.JwtUtils;
import com.matias.studying.dto.request.AuthenticationRequest;
import com.matias.studying.dto.request.RegisterRequest;
import com.matias.studying.dto.response.AuthenticationResponse;
import com.matias.studying.dto.response.RegisterResponse;
import com.matias.studying.mapper.UserMapper;
import com.matias.studying.model.RoleEntity;
import com.matias.studying.repository.IRoleRepository;
import com.matias.studying.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.matias.studying.model.UserEntity;
import com.matias.studying.service.abstraction.IAuthenticationService;

@Service
public class AuthenticationService implements IAuthenticationService {


	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IRoleRepository roleRepository;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private AuthenticationManager authManager;

	@Override
	public RegisterResponse register(RegisterRequest registerRequest) {
		if (userRepository.findByEmail(registerRequest.getEmail()) != null) {
			throw new UserAlreadyExistException("Email is already in use.");
		}

		RoleEntity userRoleEntity = roleRepository.findByName(Role.USER.getFullRoleName());
		if (userRoleEntity == null) {
			throw new EntityNotFoundException("Missing record in role table.");
		}

		UserEntity userEntity = userMapper.toUserEntity(registerRequest);
		userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
		userEntity.setSoftDeleted(false);
		userEntity.setRole(userRoleEntity);
		userEntity = userRepository.save(userEntity);
		
		// TODO Se debería enviar un email de bienvenida

		RegisterResponse registerResponse = userMapper.toRegisterResponse(userEntity);
		registerResponse.setToken(jwtUtils.generateToken(userEntity));
		return registerResponse;
	}

	@Override
	public AuthenticationResponse login(AuthenticationRequest authenticationRequest) {
		Authentication authentication;
		try {
			authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getEmail(), authenticationRequest.getPassword()));
		} catch (Exception e) {
			throw new InvalidCredentialsException("Invalid email or password.");
		}

		String jwt = jwtUtils.generateToken((UserDetails) authentication.getPrincipal());
		return new AuthenticationResponse(jwt);
	}
	
}
