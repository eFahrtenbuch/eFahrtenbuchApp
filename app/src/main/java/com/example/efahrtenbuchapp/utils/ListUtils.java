package com.example.efahrtenbuchapp.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListUtils {
	public static <T> ArrayList<T> createArrayListOf(T... t){
		ArrayList<T> al = new ArrayList<T>();
		al.addAll(Arrays.asList(t));
		return al;
	}

	public static <T> List<T> removeFromList(List<T> list, List<T> elementsToBeRemoved){
		list.removeAll(elementsToBeRemoved);
		return list;
	}
}
