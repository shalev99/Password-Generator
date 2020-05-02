package com.RomTal.java;

import java.security.SecureRandom;

/***
 * Password Generator Manger
 */
public class Password implements IModel {
//  passwords Generator at least 10 characters long.
	private final byte PASSWORD_LENGTH = 16;
	//This string holds all the possible values that a generated password might contain.
	private final String CHARACTER_DICTIONARY = "abcdefghijklmnopqrstuvwxyz"
											  + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
											  + "1234567890"
											  + "!?@#$%";
	private String password;
	private SecureRandom secureGenerator = new SecureRandom();
	//Constructor that generates a 16 character string, that will be used to create a password.zB7yI6@quL5SS6pd
	public Password() {
		password = "";
	}
	public Password(String existingPass) {
		password = existingPass;
	}
	public Password(Password object2) {
		password = object2.password;
	}
	@Override
	public void generatePassword() {
		String[] arrString = new String[PASSWORD_LENGTH]; //array that will be used for the generation of the password.
		while (!(this.hasSymbols())) { //Loop that checks for the password containing a symbol.
			for (int i = 0; i < PASSWORD_LENGTH; i++) {
				arrString[i] = Character.toString(CHARACTER_DICTIONARY.charAt(secureGenerator.nextInt(CHARACTER_DICTIONARY.length())));
			}
			password = String.join("", arrString);	
		}
	}
	@Override
	public void generatePassword(char startingChar) {
		String[] arrString = new String[PASSWORD_LENGTH]; //array that will be used for the generation of the password.
		while (!(this.hasSymbols())) { //Loop that checks for the password containing a symbol.
			arrString[0] = Character.toString(startingChar);
			for (int i = 1; i < PASSWORD_LENGTH; i++) { 
				arrString[i] = Character.toString(CHARACTER_DICTIONARY.charAt(secureGenerator.nextInt(CHARACTER_DICTIONARY.length())));
			}
			password = String.join("", arrString);	
		}
	}
	@Override
	public void generatePassword(int length) {
		String[] arrString = new String[length]; //array that will be used for the generation of the password.
		while (!(this.hasSymbols())) { //Loop that checks for the password containing a symbol.
			for (int i = 0; i < PASSWORD_LENGTH; i++) { 
				arrString[i] = Character.toString(CHARACTER_DICTIONARY.charAt(secureGenerator.nextInt(CHARACTER_DICTIONARY.length())));
			}
			password = String.join("", arrString);	
		}
	}
	@Override
	public boolean equals(String compareString) {
		return password == compareString;
	}
	@Override
	public boolean hasSymbols() {
		return (password.contains("?") || password.contains("!") || password.contains("@") || password.contains("#") || password.contains("$") || password.contains("%"));
	}
	@Override
	public String toString() {
		return password;
	}
}
