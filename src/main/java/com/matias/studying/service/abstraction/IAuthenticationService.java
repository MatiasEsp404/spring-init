package com.matias.studying.service.abstraction;

import com.matias.studying.dto.request.AuthenticationRequest;
import com.matias.studying.dto.request.RegisterRequest;
import com.matias.studying.dto.response.AuthenticationResponse;
import com.matias.studying.dto.response.RegisterResponse;

public interface IAuthenticationService {

	RegisterResponse register(RegisterRequest registerRequest);

	AuthenticationResponse login(AuthenticationRequest authenticationRequest);

}
