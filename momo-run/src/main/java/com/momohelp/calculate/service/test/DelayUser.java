package com.momohelp.calculate.service.test;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class DelayUser implements Delayed {

	@Override
	public int compareTo(Delayed o) {
		
		return 0;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return 0;
	}

}