package com.web.project.function;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Lazy
@Scope("prototype")
@Data
public class MillisecondsConverter {

	public Long convertMilliseconds(Long milliseconds) {
//        long seconds = milliseconds / 1000;
		Long minutes = milliseconds / 1000 / 60;
//        long hours = minutes / 60;

		return minutes;
	}
}
