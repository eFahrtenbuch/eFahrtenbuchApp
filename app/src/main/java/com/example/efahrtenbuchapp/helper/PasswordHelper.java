package com.example.efahrtenbuchapp.helper;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHelper {
	
	public static boolean checkPasswortRichtlinien(String passwort) {
		//TODO
//		if(passwort.length() > 4 && passwort.)
		return true;
	}
	
	public static String getEncryptedPassword(String text) throws NoSuchAlgorithmException
	{
		return PasswordHelper.bytesToHex(PasswordHelper.encyrpt(text));
	}
	
	/**
	 * �berpr�ft das als Parameter angegebene Passwort gegen das aus der Datenbank
	 * @param passwordClearText Passwort gegen das gepr�ft wird
	 * @return true wenn Passwort �bereinstimmt, false wenn nicht
	 * @throws NoSuchAlgorithmException, GeneralSecurityException 
	 * @throws Exception (Exception, NoSuchAlgorithmException)
	 */
	public static boolean comparePasswords(String passwordClearText, String hashedPassword) throws NoSuchAlgorithmException, GeneralSecurityException
	{
			return hashedPassword.equals(PasswordHelper.getEncryptedPassword(passwordClearText));
	}

	/**
	 * Prüft ob das übergebene Passwort verschlüsselt ist
	 * @param password
	 * @return
	 * @throws IllegalArgumentException
	 */
	public static void isPasswordEncrypted(String password) throws IllegalArgumentException{
		if(password.length() != 64) throw new IllegalArgumentException("FEHLER: Passwort muss ein SHA-256 Hash mit einer länge von 64 Zeichen sein");
	}

	private static byte[] encyrpt(String text) throws NoSuchAlgorithmException
	{
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		return digest.digest(text.getBytes(StandardCharsets.UTF_8));
	}
	
	private static String bytesToHex(byte[] hash) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if(hex.length() == 1) hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}
}
