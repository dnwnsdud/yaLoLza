package com.web.project.metrics.count;

import com.web.project.metrics.Counter;
import com.web.project.system.Util;

public class Connect {
	public Connect(String...strings) {
		while(strings.length > 0) {
			Counter.Increment(Util.joinToken("_",strings), 1);
			strings = Util.slice(strings.length - 1, strings);
		}
	}
}
