package com.web.project.system;


/***
 * 대소문자 무시 정규식 함수 목록
 */
public final class Regex {
	private Regex() {}
	public static boolean isEquals(String value, String contain) {
		return value.matches("(?i)^" + contain + "$");
	}
	public static boolean isContain(String value, String contain) {
		return value.matches("(?i)^.*" + contain + ".*$");
	}
	public static boolean isEndsWith(String value, String end) {
		return value.matches("(?i)^.*" + end + "$");
	}
	public static boolean isStartsWith(String value, String start) {
		return value.matches("(?i)^" + start + ".*$");
	}
	public static boolean isExtension(String value, String extension) {
		return value.matches("(?i)." + extension + "$");
	}
	public static boolean isNumber(String value) {
		return value.matches("^\\d$");
	}
	public static boolean isEmail(String value) {
		return value.matches("(?i)^[\\w\\-\\.]+@[\\w\\-\\.]+$");
	}
	public static boolean isPhone(String value) {
		return value.matches("^\\d{2,3}-\\d{3,4}-\\d{4}$");
	}
	public static boolean isDate(String value) {
		return value.matches("^\\d{2,4}[\\-\\/]\\d{1,2}[\\-\\/]\\d{1,2}$");
	}
	public static boolean isTime(String value) {
		return value.matches("^\\d{1,2}:\\d{1,2}:\\d{1,2}$");
	}
	public static boolean isDomain(String value) {
		return value.matches("(?i)^https?:\\/\\/[\\w\\-\\.]+$");
	}
}
