package com.matias.studying.bigtest.auth;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.matias.studying.config.security.constants.Paths;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.matias.studying.bigtest.util.BigTest;

public class GetAuthenticatedUserDetailsIntegrationTest extends BigTest {

	@Test
	public void shouldReturnUserWhenHasUserRole() throws Exception {
		mockMvc.perform(get(Paths.AUTH + Paths.ME).contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.AUTHORIZATION, getAuthorizationTokenForStandardUser()))
				.andExpect(jsonPath("$.firstName", equalTo("Fernando")))
				.andExpect(jsonPath("$.lastName", equalTo("Gaspari")))
				.andExpect(jsonPath("$.email", equalTo("fernando@gmail.com")))
				.andExpect(jsonPath("$.role", equalTo("ROLE_USER")))
				.andExpect(status().isOk());
	}

	@Test
	public void shouldReturnUserWhenHasAdminRole() throws Exception {
		mockMvc.perform(get(Paths.AUTH + Paths.ME).contentType(MediaType.APPLICATION_JSON)
						.header(HttpHeaders.AUTHORIZATION, getAuthorizationTokenForAdminUser()))
				.andExpect(jsonPath("$.firstName", equalTo("Matias")))
				.andExpect(jsonPath("$.lastName", equalTo("Espinola")))
				.andExpect(jsonPath("$.email", equalTo("matias@gmail.com")))
				.andExpect(jsonPath("$.role", equalTo("ROLE_ADMIN")))
				.andExpect(status().isOk());
	}

}
