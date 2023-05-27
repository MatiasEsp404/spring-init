package com.matias.studying.bigtest.auth;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.matias.studying.config.security.constants.Paths;
import org.junit.Test;
import org.springframework.http.MediaType;

import com.matias.studying.bigtest.util.BigTest;
import com.matias.studying.dto.request.RegisterRequest;
import com.matias.studying.model.UserEntity;

public class RegisterAuthenticationIntegrationTest extends BigTest {

	private String firstname = "Jere";
	private String lastname = "Bait";
	private String email = "jerebait@mail.com";
	private String password = "pass1234";
	
	@Test
	public void shouldCreateNewUserWhenGivenDataIsValid() throws Exception {
		mockMvc.perform(post(Paths.AUTH + Paths.REGISTER)
				.content(objectMapper.writeValueAsString(
						RegisterRequest.builder()
						.firstName(firstname)
						.lastName(lastname)
						.email(email)
						.password(password)
						.build()))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.firstName", equalTo(firstname)))
				.andExpect(jsonPath("$.lastName", equalTo(lastname)))
				.andExpect(jsonPath("$.email", equalTo(email)))
				.andExpect(jsonPath("$.token", notNullValue()))
				.andExpect(status().isCreated());
		UserEntity newUser = userRepository.findByEmail(email);
		assertNotNull(newUser);
		assertEquals(firstname, newUser.getFirstName());
		assertEquals(lastname, newUser.getLastName());
	}

	@Test
	public void shouldReturnBadRequestWhenFirstNameIsNull() throws Exception {

		mockMvc.perform(post(Paths.AUTH + Paths.REGISTER)
				.content(objectMapper.writeValueAsString(
						RegisterRequest.builder()
						.lastName(lastname)
						.email(email)
						.password(password)
						.build()))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.statusCode", equalTo(400)))
				.andExpect(jsonPath("$.message", equalTo("Invalid input data.")))
				.andExpect(jsonPath("$.moreInfo", hasSize(1)))
				.andExpect(jsonPath("$.moreInfo", hasItem("The first name must not be null")))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldReturnBadRequestWhenFirstNameContainsNumbers() throws Exception {

		mockMvc.perform(post(Paths.AUTH + Paths.REGISTER)
				.content(objectMapper.writeValueAsString(
						RegisterRequest.builder()
						.firstName("J3r3")
						.lastName(lastname)
						.email(email)
						.password(password)
						.build()))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.statusCode", equalTo(400)))
				.andExpect(jsonPath("$.message", equalTo("Invalid input data.")))
				.andExpect(jsonPath("$.moreInfo", hasSize(1)))
				.andExpect(jsonPath("$.moreInfo", hasItem("First name can contain letters and spaces")))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldReturnBadRequestWhenLastNameIsNull() throws Exception {
		mockMvc.perform(post(Paths.AUTH + Paths.REGISTER)
				.content(objectMapper.writeValueAsString(
						RegisterRequest.builder()
						.firstName(firstname)
						.email(email)
						.password(password)
						.build()))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.statusCode", equalTo(400)))
				.andExpect(jsonPath("$.message", equalTo("Invalid input data.")))
				.andExpect(jsonPath("$.moreInfo", hasSize(1)))
				.andExpect(jsonPath("$.moreInfo", hasItem("The last name must not be null")))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldReturnBadRequestWhenLastNameContainsNumbers() throws Exception {
		mockMvc.perform(post(Paths.AUTH + Paths.REGISTER)
				.content(objectMapper.writeValueAsString(
						RegisterRequest.builder()
						.firstName(firstname)
						.lastName("B41t")
						.email(email)
						.password(password)
						.build()))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.statusCode", equalTo(400)))
				.andExpect(jsonPath("$.message", equalTo("Invalid input data.")))
				.andExpect(jsonPath("$.moreInfo", hasSize(1)))
				.andExpect(jsonPath("$.moreInfo", hasItem("Last name can contain letters and spaces")))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldReturnBadRequestWhenEmailIsNull() throws Exception {
		mockMvc.perform(post(Paths.AUTH + Paths.REGISTER)
				.content(objectMapper.writeValueAsString(
						RegisterRequest.builder()
						.firstName(firstname)
						.lastName(lastname)
						.password(password)
						.build()))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.statusCode", equalTo(400)))
				.andExpect(jsonPath("$.message", equalTo("Invalid input data.")))
				.andExpect(jsonPath("$.moreInfo", hasSize(1)))
				.andExpect(jsonPath("$.moreInfo", hasItem("The email must not be null")))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldReturnConflictWhenEmailIsAlreadyExist() throws Exception {
		mockMvc.perform(post(Paths.AUTH + Paths.REGISTER)
				.content(objectMapper.writeValueAsString(RegisterRequest.builder()
						.firstName(firstname)
						.lastName(lastname)
						.email(super.USER_EMAIL)
						.password(password)
						.build()))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.statusCode", equalTo(409)))
				.andExpect(jsonPath("$.message", equalTo("Email is already in use.")))
				.andExpect(jsonPath("$.moreInfo", hasSize(1)))
				.andExpect(jsonPath("$.moreInfo",
						hasItem("The server could not complete the user registration because the email address"
								+ " entered is already in use.")))
				.andExpect(status().isConflict());
	}

	@Test
	public void shouldReturnBadRequestWhenEmailHaveInvalidFormat() throws Exception {
		mockMvc.perform(post(Paths.AUTH + Paths.REGISTER)
				.content(objectMapper.writeValueAsString(
						RegisterRequest.builder()
						.firstName(firstname)
						.lastName(lastname)
						.email("invalidFormat")
						.password(password)
						.build()))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.statusCode", equalTo(400)))
				.andExpect(jsonPath("$.message", equalTo("Invalid input data.")))
				.andExpect(jsonPath("$.moreInfo", hasSize(1)))
				.andExpect(jsonPath("$.moreInfo", hasItem("The email has invalid format.")))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldReturnBadRequestWhenPasswordIsNull() throws Exception {
		mockMvc.perform(post(Paths.AUTH + Paths.REGISTER)
				.content(objectMapper.writeValueAsString(
						RegisterRequest.builder()
						.firstName(firstname)
						.lastName(lastname)
						.email(email)
						.build()))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.statusCode", equalTo(400)))
				.andExpect(jsonPath("$.message", equalTo("Invalid input data.")))
				.andExpect(jsonPath("$.moreInfo", hasSize(1)))
				.andExpect(jsonPath("$.moreInfo", hasItem("The password must not be null")))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldReturnBadRequestWhenPasswordIsTooShort() throws Exception {

		mockMvc.perform(post(Paths.AUTH + Paths.REGISTER)
				.content(objectMapper.writeValueAsString(RegisterRequest.builder().firstName(firstname)
								.lastName(lastname).email(email).password("pass").build()))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.statusCode", equalTo(400)))
				.andExpect(jsonPath("$.message", equalTo("Invalid input data.")))
				.andExpect(jsonPath("$.moreInfo", hasSize(1)))
				.andExpect(jsonPath("$.moreInfo", hasItem("The password must be between 8 and 16 characters.")))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldReturnBadRequestWhenPasswordIsTooLong() throws Exception {

		mockMvc.perform(post(Paths.AUTH + Paths.REGISTER)
				.content(objectMapper.writeValueAsString(RegisterRequest.builder()
						.firstName(firstname)
						.lastName(lastname)
						.email(email)
						.password("MyPasswordIsTooLong").build()))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.statusCode", equalTo(400)))
				.andExpect(jsonPath("$.message", equalTo("Invalid input data.")))
				.andExpect(jsonPath("$.moreInfo", hasSize(1)))
				.andExpect(jsonPath("$.moreInfo", hasItem("The password must be between 8 and 16 characters.")))
				.andExpect(status().isBadRequest());
	}

}
