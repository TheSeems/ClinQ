package me.theseems.clinq.test.utils;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StringUtils {
	public static List<String> randomStrings(int size, int minLength, int maxLength) {
		Random random = new Random();
		return IntStream.range(0, size)
			.mapToObj(i -> randomString(random, minLength, maxLength))
			.collect(Collectors.toUnmodifiableList());
	}

	private static String randomString(Random random, int minLength, int maxLength) {
		return IntStream
			.range(0, random.nextInt(maxLength - minLength) + minLength)
			.map(i -> random.nextInt(26))
			.mapToObj(i -> Character.toString('a' + i))
			.collect(Collectors.joining(""));
	}
}
