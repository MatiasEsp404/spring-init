package com.matias.studying.service.abstraction;

import com.matias.studying.dto.request.UpdateUserRequest;
import com.matias.studying.dto.response.ListUsersResponse;
import com.matias.studying.dto.response.UserResponse;

public interface IUserService {

  ListUsersResponse listActiveUsers();

  UserResponse getUserAuthenticated();

  void update(Integer id, UpdateUserRequest updateUserRequest);

  void delete(Integer id);


}
