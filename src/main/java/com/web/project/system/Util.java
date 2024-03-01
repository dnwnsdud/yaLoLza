package com.web.project.system;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public final class Util {

	public static <T> T[] slice(int begin, int end, T[] args) {
		if(end <= begin) return Arrays.copyOfRange(args, 0, 0);
		return Arrays.copyOfRange(args, begin, end);
	}

	public static <T> T[] slice(int end, T[] args) {
		if(end <= 0) return Arrays.copyOfRange(args, 0, 0);
		return Arrays.copyOfRange(args, 0, end);
	}
	
	public static String join(String...strings) {
		String result = "";
		for(int i = 0; i < strings.length; i += 1) {
			result += strings[i];
			if(strings.length - 1 != i) result += ".";
		}
		return result;
	}
	public static String joinToken(String token, String...strings) {
		String result = "";
		for(int i = 0; i < strings.length; i += 1) {
			result += strings[i];
			if(strings.length - 1 != i) result += token;
		}
		return result;
	}
	
	
	private Util() {}
}
