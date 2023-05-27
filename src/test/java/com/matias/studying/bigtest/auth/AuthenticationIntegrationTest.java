package com.matias.studying.bigtest.auth;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.matias.studying.bigtest.util.BigTest;
import com.matias.studying.config.security.constants.Paths;
import com.matias.studying.dto.request.AuthenticationRequest;
import org.junit.Test;
import org.springframework.http.MediaType;

public class AuthenticationIntegrationTest extends BigTest {

	@Test
	public void shouldReturnTokenWhenCredentialsAreValid() throws Exception {

		mockMvc.perform(post(Paths.AUTH + Paths.LOGIN)
				.content(objectMapper.writeValueAsString(
						AuthenticationRequest.builder().email("matias@gmail.com").password("Test1234").build()))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.token", notNullValue()))
				.andExpect(status().isOk());
	}

	@Test
	public void shouldReturnIsUnauthorizedStatusCodeWhenCredentialsAreInvalid() throws Exception {

		mockMvc.perform(post(Paths.AUTH + Paths.LOGIN)
				.content(objectMapper.writeValueAsString(
						AuthenticationRequest.builder().email("matias@gmail.com").password("wrongPassword").build()))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.statusCode", equalTo(401)))
				.andExpect(jsonPath("$.message", equalTo("Invalid email or password.")))
				.andExpect(jsonPath("$.moreInfo", hasSize(1)))
				.andExpect(jsonPath("$.moreInfo",
						hasItem("The server cannot return a response due to invalid credentials.")))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void shouldReturnBadRequestStatusCodeWhenCredentialsHaveInvalidFormat() throws Exception {

		mockMvc.perform(post(Paths.AUTH + Paths.LOGIN)
				.content(objectMapper.writeValueAsString(
						AuthenticationRequest.builder().email("incorrectFormatEmail").password("pass").build()))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.statusCode", equalTo(400)))
				.andExpect(jsonPath("$.message", equalTo("Invalid input data.")))
				.andExpect(jsonPath("$.moreInfo", hasSize(2)))
				.andExpect(jsonPath("$.moreInfo",
						hasItems("The password must be between 8 and 16 characters.", "The email has invalid format.")))
				.andExpect(status().isBadRequest());
	}

}
