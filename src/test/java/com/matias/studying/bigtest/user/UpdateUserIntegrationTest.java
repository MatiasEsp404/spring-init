package com.matias.studying.bigtest.user;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.matias.studying.config.security.constants.Paths;
import java.util.Optional;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.matias.studying.bigtest.util.BigTest;
import com.matias.studying.dto.request.UpdateUserRequest;
import com.matias.studying.model.UserEntity;

public class UpdateUserIntegrationTest extends BigTest {

	@Test
	public void shouldUpdateUserWhenUserHasAdminRole() throws Exception {
		UserEntity randomUser = getRandomUser();

		mockMvc.perform(patch(Paths.USERS + Paths.ID, String.valueOf(randomUser.getId()))
				.content(objectMapper.writeValueAsString(UpdateUserRequest.builder().lastName("Gordon").build()))
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, getAuthorizationTokenForAdminUser()))
				.andExpect(status().isNoContent());

		Optional<UserEntity> updatedUser = userRepository.findById(randomUser.getId());
		assertTrue(updatedUser.isPresent());
		assertEquals("Gordon", updatedUser.get().getLastName());

		cleanUsersData(randomUser);
	}

	@Test
	public void shouldUpdateUserWhenUserHasStandardUserRole() throws Exception {
		UserEntity randomUser = getRandomUser();

		mockMvc.perform(patch(Paths.USERS + Paths.ID, String.valueOf(randomUser.getId()))
				.content(objectMapper.writeValueAsString(UpdateUserRequest.builder().lastName("Gordon").build()))
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, getAuthorizationTokenForStandardUser()))
				.andExpect(status().isNoContent());

		Optional<UserEntity> updatedUser = userRepository.findById(randomUser.getId());
		assertTrue(updatedUser.isPresent());
		assertEquals("Gordon", updatedUser.get().getLastName());

		cleanUsersData(randomUser);
	}

	@Test
	public void shouldReturnForbiddenErrorResponseWhenTokenIsNotSent() throws Exception {
		UserEntity randomUser = getRandomUser();

		mockMvc.perform(patch(Paths.USERS + Paths.ID, String.valueOf(randomUser.getId()))
				.content(objectMapper.writeValueAsString(UpdateUserRequest.builder().lastName("Gordon").build()))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.statusCode", equalTo(403)))
				.andExpect(jsonPath("$.message",
						equalTo("Access denied. Please, try to login again or contact your admin.")))
				.andExpect(status().isForbidden());

		cleanUsersData(randomUser);
	}

	@Test
	public void shouldReturnNotFoundErrorResponseWhenUserNotExist() throws Exception {
		String nonExistUserId = "1000000";
		mockMvc.perform(patch(Paths.USERS + Paths.ID, nonExistUserId)
				.content(objectMapper.writeValueAsString(UpdateUserRequest.builder().lastName("Gordon").build()))
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, getAuthorizationTokenForAdminUser()))
				.andExpect(jsonPath("$.statusCode", equalTo(404)))
				.andExpect(jsonPath("$.message", equalTo("Entity not found.")))
				.andExpect(jsonPath("$.moreInfo", hasSize(1)))
				.andExpect(jsonPath("$.moreInfo", hasItem("User not found."))).andExpect(status().isNotFound());
	}

	@Test
	public void shouldReturnBadRequestWhenPasswordIsTooLong() throws Exception {
		UserEntity randomUser = getRandomUser();

		mockMvc.perform(patch(Paths.USERS + Paths.ID, String.valueOf(randomUser.getId()))
				.content(objectMapper.writeValueAsString(UpdateUserRequest.builder()
						.password("0123456789101112131415").build()))
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, getAuthorizationTokenForAdminUser()))
				.andExpect(jsonPath("$.statusCode", equalTo(400)))
				.andExpect(jsonPath("$.message", equalTo("Invalid input data.")))
				.andExpect(jsonPath("$.moreInfo", hasSize(1)))
				.andExpect(jsonPath("$.moreInfo", hasItem("The password must be between 8 and 16 characters.")))
				.andExpect(status().isBadRequest());

		cleanUsersData(randomUser);
	}

	@Test
	public void shouldReturnBadRequestWhenPasswordIsTooShort() throws Exception {
		UserEntity randomUser = getRandomUser();

		mockMvc.perform(patch(Paths.USERS + Paths.ID, String.valueOf(randomUser.getId()))
				.content(objectMapper.writeValueAsString(UpdateUserRequest.builder().password("0123").build()))
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, getAuthorizationTokenForAdminUser()))
				.andExpect(jsonPath("$.statusCode", equalTo(400)))
				.andExpect(jsonPath("$.message", equalTo("Invalid input data.")))
				.andExpect(jsonPath("$.moreInfo", hasSize(1)))
				.andExpect(jsonPath("$.moreInfo", hasItem("The password must be between 8 and 16 characters.")))
				.andExpect(status().isBadRequest());

		cleanUsersData(randomUser);
	}

	@Test
	public void shouldReturnBadRequestWhenFirstNameContainsNumbers() throws Exception {
		UserEntity randomUser = getRandomUser();

		mockMvc.perform(patch(Paths.USERS + Paths.ID, String.valueOf(randomUser.getId()))
				.content(objectMapper.writeValueAsString(UpdateUserRequest.builder().firstName("G0rd0n").build()))
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, getAuthorizationTokenForAdminUser()))
				.andExpect(jsonPath("$.statusCode", equalTo(400)))
				.andExpect(jsonPath("$.message", equalTo("Invalid input data.")))
				.andExpect(jsonPath("$.moreInfo", hasSize(1)))
				.andExpect(jsonPath("$.moreInfo", hasItem("Name can contain letters and spaces")))
				.andExpect(status().isBadRequest());

		cleanUsersData(randomUser);
	}

	@Test
	public void shouldReturnBadRequestWhenLastNameContainsNumbers() throws Exception {
		UserEntity randomUser = getRandomUser();

		mockMvc.perform(patch(Paths.USERS + Paths.ID, String.valueOf(randomUser.getId()))
				.content(objectMapper.writeValueAsString(UpdateUserRequest.builder().lastName("Fr33m4n").build()))
				.contentType(MediaType.APPLICATION_JSON)
				.header(HttpHeaders.AUTHORIZATION, getAuthorizationTokenForAdminUser()))
				.andExpect(jsonPath("$.statusCode", equalTo(400)))
				.andExpect(jsonPath("$.message", equalTo("Invalid input data.")))
				.andExpect(jsonPath("$.moreInfo", hasSize(1)))
				.andExpect(jsonPath("$.moreInfo", hasItem("Last name can contain letters and spaces")))
				.andExpect(status().isBadRequest());

		cleanUsersData(randomUser);
	}

}
