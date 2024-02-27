package com.web.project.metrics;

import java.util.LinkedList;
import java.util.List;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;

public final class Gauge {
	private Gauge() {};
	public static <T extends Number> void Set(String key, T data) {
		List<Tag> tags = new LinkedList<Tag>();
		tags.add(Tag.of("type", "gauge"));
		Metrics.gauge(key, tags, data);
	}

}
