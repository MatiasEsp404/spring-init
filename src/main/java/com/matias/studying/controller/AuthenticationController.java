package com.matias.studying.controller;

import com.matias.studying.config.security.constants.Paths;
import com.matias.studying.dto.request.AuthenticationRequest;
import com.matias.studying.dto.request.RegisterRequest;
import com.matias.studying.dto.response.AuthenticationResponse;
import com.matias.studying.dto.response.RegisterResponse;
import com.matias.studying.dto.response.UserResponse;
import com.matias.studying.service.abstraction.IAuthenticationService;
import com.matias.studying.service.abstraction.IUserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = Paths.AUTH)
public class AuthenticationController {

	@Autowired
	private IAuthenticationService authService;

	@Autowired
	private IUserService userService;

	@PostMapping(path = Paths.REGISTER, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
		return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(registerRequest));
	}

	@PostMapping(path = Paths.LOGIN, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AuthenticationResponse> login(
			@Valid @RequestBody AuthenticationRequest authenticationRequest) {
		return ResponseEntity.ok().body(authService.login(authenticationRequest));
	}

	@GetMapping(path = Paths.ME, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UserResponse> getUser() {
		return ResponseEntity.ok().body(userService.getUserAuthenticated());
	}

}