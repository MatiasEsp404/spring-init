package com.matias.studying.controller;

import com.matias.studying.config.security.constants.Paths;
import com.matias.studying.dto.request.UpdateUserRequest;
import com.matias.studying.dto.response.ListUsersResponse;
import com.matias.studying.service.abstraction.IUserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = Paths.USERS)
public class UserController {

	@Autowired
	private IUserService userService;


	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ListUsersResponse> listActiveUsers() {
		return ResponseEntity.ok().body(userService.listActiveUsers());
	}

	@DeleteMapping(value = Paths.ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		userService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping(value = Paths.ID, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> update(@PathVariable Integer id, @Valid @RequestBody UpdateUserRequest updateUserRequest) {
		userService.update(id, updateUserRequest);
		return ResponseEntity.noContent().build();
	}

}