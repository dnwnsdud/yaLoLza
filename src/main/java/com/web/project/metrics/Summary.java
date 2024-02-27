package com.web.project.metrics;

import java.util.LinkedList;
import java.util.List;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;

public final class Summary {

	private Summary() {};
	public static void Set(String key, Double data) {
		List<Tag> tags = new LinkedList<Tag>();
		tags.add(Tag.of("type", "summary"));
		Metrics.summary(key, tags).record(data);
	}
}
