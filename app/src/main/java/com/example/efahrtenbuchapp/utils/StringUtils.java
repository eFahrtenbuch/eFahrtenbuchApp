package com.example.efahrtenbuchapp.utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
public class StringUtils {
	public static boolean containsANumber(String string) {
		ArrayList<Character> numberList = ListUtils.createArrayListOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9');
		Optional<Character> result = numberList.stream().filter(number -> containsChar(string, (char) number)).findAny();
		return result.isPresent();
	}
	
	public static boolean containsAnyChar(List<Character> charList, String string) {
		Optional<Character> result = charList.stream().filter(charAsNumber -> StringUtils.containsChar(string, (char) charAsNumber)).findAny();
		return result.isPresent();
	}
	
	public static boolean containsChar(String string, char character) {
		OptionalInt result = string.chars().filter(testingChar -> testingChar == (int) character).findAny();
		return result.isPresent();
	}
	
	public static boolean isBlank(String string) {
		if(string != null) {
			return string.isEmpty();
		}
		return true;
	}
	
	public static boolean notBlank(String string) {
		return !isBlank(string);
	}
}
