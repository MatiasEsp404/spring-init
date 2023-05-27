package com.matias.studying.service;

import com.matias.studying.service.abstraction.IUserService;
import com.matias.studying.config.exception.runtime.EntityNotFoundException;
import com.matias.studying.config.security.util.SecurityUtils;
import com.matias.studying.dto.request.UpdateUserRequest;
import com.matias.studying.dto.response.ListUsersResponse;
import com.matias.studying.dto.response.UserResponse;
import com.matias.studying.mapper.UserMapper;
import com.matias.studying.mapper.updater.UserUpdater;
import com.matias.studying.repository.IUserRepository;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.matias.studying.model.UserEntity;

@Service
public class UserService implements UserDetailsService, IUserService {

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private SecurityUtils securityUtils;
	
	@Autowired
	private UserUpdater userUpdater;

	@Override
	public ListUsersResponse listActiveUsers() {
		List<UserEntity> listUserEntities = userRepository.findAllActiveUsers();
		ListUsersResponse listUsersResponse = new ListUsersResponse();
		listUsersResponse.setUsers(userMapper.toListUserResponse(listUserEntities));
		return listUsersResponse;
	}

	@Override
	public UserResponse getUserAuthenticated() {
		return userMapper.toUserResponse((UserEntity) securityUtils.getUserAuthenticated());
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return getUser(email);
	}

	private UserEntity getUser(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null) {
			throw new UsernameNotFoundException("User not found.");
		}
		return userEntity;
	}

	@Override
	public void delete(Integer id) {
		UserEntity userEntity = findBy(id);
		userEntity.setSoftDeleted(true);
		userRepository.save(userEntity);
	}

	private UserEntity findBy(Integer id) {
		Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
		if (!optionalUserEntity.isPresent() || Boolean.TRUE.equals(optionalUserEntity.get().getSoftDeleted())) {
			throw new EntityNotFoundException("User not found.");
		}
		return optionalUserEntity.get();
	}

	@Override
	public void update(Integer id, UpdateUserRequest updateUserRequest) {
		UserEntity userEntity = findBy(id);
		UserEntity userUpdated = userUpdater.update(updateUserRequest, userEntity);
		userRepository.save(userUpdated);
	}

}