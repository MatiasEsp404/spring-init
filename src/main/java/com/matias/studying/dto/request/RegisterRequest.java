package com.matias.studying.dto.request;

import com.matias.studying.config.security.constants.Validations;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

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
public class RegisterRequest {

	@NotNull(message = "The first name must not be null")
	@Pattern(regexp = Validations.CHARACTERS_WITH_WHITE_SPACES, message = "First name can contain letters and spaces")
	private String firstName;

	@NotNull(message = "The last name must not be null")
	@Pattern(regexp = Validations.CHARACTERS_WITH_WHITE_SPACES, message = "Last name can contain letters and spaces")
	private String lastName;

	@NotNull(message = "The email must not be null")
	@Email(message = "The email has invalid format.")
	private String email;

	@NotNull(message = "The password must not be null")
	@Length(min = 8, max = 16, message = "The password must be between 8 and 16 characters.")
	private String password;

}
