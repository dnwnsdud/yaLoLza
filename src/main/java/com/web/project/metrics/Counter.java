package com.web.project.metrics;

import java.util.LinkedList;
import java.util.List;

import io.micrometer.core.instrument.*;

public final class Counter {
	private Counter() {};
	public static void Increment(String key, double data) {
		List<Tag> tags = new LinkedList<Tag>();
		tags.add(Tag.of("type", "counter"));
		Metrics.counter(key, tags).increment(data);
	}
	public static void Decrement(String key, double data) {
		List<Tag> tags = new LinkedList<Tag>();
		tags.add(Tag.of("type", "counter"));
		Metrics.counter(key, tags).increment(data * -1);
	}
}
