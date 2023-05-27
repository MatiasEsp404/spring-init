package com.matias.studying.dto.request;

import com.matias.studying.config.security.constants.Validations;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateUserRequest {

	@Nullable
	@Pattern(regexp = Validations.CHARACTERS_WITH_WHITE_SPACES, message = "Name can contain letters and spaces")
	private String firstName;

	@Nullable
	@Pattern(regexp = Validations.CHARACTERS_WITH_WHITE_SPACES, message = "Last name can contain letters and spaces")
	private String lastName;

	@Nullable
	@Email(message = "The email has invalid format.")
	private String email;
	
	@Nullable
	@Length(min = 8, max = 16, message = "The password must be between 8 and 16 characters.")
	private String password;

}
