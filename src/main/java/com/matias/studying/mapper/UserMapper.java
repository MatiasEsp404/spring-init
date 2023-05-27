package com.matias.studying.mapper;

import com.matias.studying.dto.response.RegisterResponse;
import com.matias.studying.dto.response.UserResponse;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.matias.studying.dto.request.RegisterRequest;
import com.matias.studying.model.UserEntity;

@Component
public class UserMapper {

	public UserEntity toUserEntity(RegisterRequest registerRequest) {
		return UserEntity.builder()
				.firstName(registerRequest.getFirstName())
				.lastName(registerRequest.getLastName())
				.email(registerRequest.getEmail())
				.password(registerRequest.getPassword())
				.build();
	}

	public RegisterResponse toRegisterResponse(UserEntity userEntity) {
		return RegisterResponse.builder()
				.id(userEntity.getId())
				.firstName(userEntity.getFirstName())
				.lastName(userEntity.getLastName())
				.email(userEntity.getEmail())
				.build();
	}

	public List<UserResponse> toListUserResponse(List<UserEntity> listUserEntities) {
		List<UserResponse> userResponses = new ArrayList<>();
		for (UserEntity userEntity : listUserEntities) {
			userResponses.add(toUserResponse(userEntity));
		}
		return userResponses;
	}

	public UserResponse toUserResponse(UserEntity userEntity) {
		return UserResponse.builder()
				.id(userEntity.getId())
				.firstName(userEntity.getFirstName())
				.lastName(userEntity.getLastName())
				.email(userEntity.getEmail())
				.role(userEntity.getRole().getName())
				.build();
	}

}
