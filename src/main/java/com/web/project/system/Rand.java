package com.web.project.system;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;

/***
 * 무작위 정보 추출 함수 목록
 */
public final class Rand {
	private static final Random random = new Random();
	private Rand() {}
	public static void setSeed(Long seed) { random.setSeed(seed); }
	public static <T> Stream<T> Choise(Collection<T> collection, Integer size) { return Stream.generate(()->Choise(collection)).limit(size); }
	public static <T> T Choise(Collection<T> collection){
		Integer target = Int(collection.size());
		if(target == 0) return null;
		Iterator<T> iter = collection.iterator();
		while(target != 0) {
			iter.next(); target -= 1;
		}
		return iter.next();
	}
	public static Stream<Integer> Ints(Integer size) { return Stream.generate(()->Int()).limit(size); }
	public static Stream<Integer> Ints(Integer end, Integer size) { return Stream.generate(()->Int(end)).limit(size); }
	public static Stream<Integer> Ints(Integer begin, Integer end, Integer size) { return Stream.generate(()->Int(begin,end)).limit(size); }
	public static Stream<Long> Longs(Integer size) { return Stream.generate(()->Long()).limit(size); }
	public static Stream<Long> Longs(Long end, Integer size) { return Stream.generate(()->Long(end)).limit(size); }
	public static Stream<Long> Longs(Long begin, Long end, Integer size) { return Stream.generate(()->Long(begin,end)).limit(size); }
	public static Stream<Float> Floats(Integer size) { return Stream.generate(()->Float()).limit(size); }
	public static Stream<Float> Floats(Float end, Integer size) { return Stream.generate(()->Float(end)).limit(size); }
	public static Stream<Float> Floats(Float begin, Float end, Integer size) { return Stream.generate(()->Float(begin,end)).limit(size); }
	public static Stream<Double> Doubles(Integer size) { return Stream.generate(()->Double()).limit(size); }
	public static Stream<Double> Doubles(Double end, Integer size) { return Stream.generate(()->Double(end)).limit(size); }
	public static Stream<Double> Doubles(Double begin, Double end, Integer size) { return Stream.generate(()->Double(begin,end)).limit(size); }
	public static Stream<Boolean> Booleans(Integer size) { return Stream.generate(()->Boolean()).limit(size); }
	public static Stream<String> Strings(Integer limit, Integer size) { return Stream.generate(()->String(limit)).limit(size); }
	public static Stream<String> Strings(Integer limit, Predicate<Integer> predicate, Integer size) { return Stream.generate(()->String(limit, predicate)).limit(size); }
	public static Integer Int() { return random.nextInt(); }
	public static Integer Int(Integer begin, Integer end) { return random.nextInt(begin, end); }
	public static Integer Int(Integer end) { return random.nextInt(end); }
	public static Long Long() { return random.nextLong(); }
	public static Long Long(Long begin, Long end) { return random.nextLong(begin, end); }
	public static Long Long(Long end) { return random.nextLong(end); }
	public static Float Float() { return random.nextFloat(); }
	public static Float Float(Float begin, Float end) { return random.nextFloat(begin, end); }
	public static Float Float(Float end) { return random.nextFloat(end); }
	public static Double Double() { return random.nextDouble(); }
	public static Double Double(Double begin, Double end) { return random.nextDouble(begin, end); }
	public static Double Double(Double end) { return random.nextDouble(end); }
	public static Boolean Boolean() { return random.nextBoolean(); }
	public static String String(Integer limit) {
		return random.ints(33,126 + 1)
					.limit(limit)
					.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
					.toString();
	}
	public static String String(Integer limit, Predicate<Integer> predicate) {
		return random.ints(33,126 + 1)
					.filter(i -> predicate.test(i))
					.limit(limit)
					.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
					.toString();
	}
}
