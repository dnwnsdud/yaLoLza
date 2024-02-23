package com.web.project.config;

import java.util.List;

import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.core.instrument.Tag;

@Component
public class ActuatorConfig {
	String key;
	String tagkey;
	String tag;

	public ActuatorConfig key(String key) {
		this.key = key;
		return this;
	}
	public ActuatorConfig tagkey(String tagkey) {
		this.tagkey = tagkey;
		return this;
	}
	public ActuatorConfig tag(String tag) {
		this.tag = tag;
		return this;
	}
	public ActuatorConfig increment() {
		List<Tag> tags = List.of(Tag.of(tagkey, tag));
		Metrics.counter(key, tags).increment();
		return this;
	}
	public ActuatorConfig increment(Double incr) {
		List<Tag> tags = List.of(Tag.of(tagkey, tag));
		Metrics.counter(key, tags).increment(incr);
		return this;
	}
	public ActuatorConfig guage(Number number) {
		Metrics.gauge(key, number);
		return this;
	}
	public ActuatorConfig summary(double data) {
		List<Tag> tags = List.of(Tag.of(tagkey, tag));
		Metrics.summary(key, tags).record(data);
		return this;
	}
}
