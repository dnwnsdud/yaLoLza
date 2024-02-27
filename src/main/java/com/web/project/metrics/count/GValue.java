package com.web.project.metrics.count;

import com.web.project.metrics.Counter;
import com.web.project.metrics.Gauge;
import com.web.project.system.Util;

public class GValue<T extends Number> {
	public GValue(T data, String...strings) {
		Gauge.Set(Util.joinToken("_",strings), data);
	}
}
