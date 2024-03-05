package com.web.project.metrics;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;

public final class Timer {
	private Timer() {};
	public static void Set(String key, Runnable act) {
		List<Tag> tags = new LinkedList<Tag>();
		tags.add(Tag.of("type", "timer"));
		io.micrometer.core.instrument.Timer timer = Metrics.timer(key, tags);
		timer.record(act);
	}

}
