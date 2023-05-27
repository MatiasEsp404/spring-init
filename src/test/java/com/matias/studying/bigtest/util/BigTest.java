package com.matias.studying.bigtest.util;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.matias.studying.StudyingApplication;
import com.matias.studying.config.security.common.Role;
import com.matias.studying.dto.request.AuthenticationRequest;
import com.matias.studying.model.RoleEntity;
import com.matias.studying.model.UserEntity;
import com.matias.studying.repository.IRoleRepository;
import com.matias.studying.repository.IUserRepository;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = "spring.main.allow-bean-definition-overriding=true",
		classes = StudyingApplication.class)
@AutoConfigureMockMvc
public abstract class BigTest {

	private final String ADMIN_EMAIL = "matias@gmail.com";
	protected final String USER_EMAIL = "fernando@gmail.com";
	private final String PASSWORD = "Test1234";

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	protected IRoleRepository roleRepository;

	@Autowired
	protected IUserRepository userRepository;

	@Autowired
	protected PasswordEncoder passwordEncoder;

	@Before
	public void setup() {
		createRoles();
		createUserData();
		deleteAllEntities();
	}

	@After
	public void tearDown() {
		deleteAllEntities();
	}

	private void createRoles() {
		if (roleRepository.count() == 0) {
			roleRepository.saveAll(Arrays.asList(buildRole(Role.USER), buildRole(Role.ADMIN)));
		}
	}

	private RoleEntity buildRole(Role role) {
		return RoleEntity.builder().name(role.getFullRoleName()).build();
	}

	private void createUserData() {
		if (userRepository.findByEmail(ADMIN_EMAIL) == null) {
			saveAdminUser();
		}
		if (userRepository.findByEmail(USER_EMAIL) == null) {
			saveStandardUser();
		}
	}

	private void saveAdminUser() {
		userRepository.save(buildUser("Matias", "Espinola", ADMIN_EMAIL, Role.ADMIN));
	}

	private void saveStandardUser() {
		userRepository.save(buildUser("Fernando", "Gaspari", USER_EMAIL, Role.USER));
	}

	private UserEntity buildUser(String firstName, String lastName, String email, Role role) {
		return UserEntity.builder().firstName(firstName).lastName(lastName).email(email)
				.password(passwordEncoder.encode(PASSWORD)).role(roleRepository.findByName(role.getFullRoleName()))
				.softDeleted(false).build();
	}

	private void deleteAllEntities() {
		// Clean up the database
	}

	protected UserEntity getRandomUser() {
		return userRepository.save(buildUser("Bruce", "Wayne", "bruce@wayne.com", Role.USER));
	}

	protected String getAuthorizationTokenForStandardUser() throws Exception {
		return getAuthorizationTokenForUser(USER_EMAIL);
	}

	protected String getAuthorizationTokenForAdminUser() throws Exception {
		return getAuthorizationTokenForUser(ADMIN_EMAIL);
	}

	private String getAuthorizationTokenForUser(String email) throws Exception {
		String content = mockMvc
				.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(
								AuthenticationRequest.builder().email(email).password(PASSWORD).build())))
				.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

		return JsonPath.read(content, "$.token");
	}

	protected void cleanUsersData(UserEntity... users) {
		userRepository.deleteAllInBatch(Arrays.asList(users));
	}

}
