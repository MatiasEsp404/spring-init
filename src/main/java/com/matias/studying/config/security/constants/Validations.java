package com.matias.studying.config.security.constants;

public abstract class Validations {

	public static final String ALPHANUMERIC_WITHOUT_WHITE_SPACES = "^[\\w.]+$";
	public static final String ALPHANUMERIC_WITH_WHITE_SPACES = "^[\\w\\s]+$";
	public static final String CHARACTERS_WITH_WHITE_SPACES = "^\\p{L}+[\\p{L}\\s]*$";

}
