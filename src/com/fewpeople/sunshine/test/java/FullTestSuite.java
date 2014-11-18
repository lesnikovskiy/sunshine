package com.fewpeople.sunshine.test.java;

import junit.framework.Test;
import android.test.suitebuilder.TestSuiteBuilder;

public class FullTestSuite {
	public static Test suit() {
		return new TestSuiteBuilder(FullTestSuite.class)
			.includeAllPackagesUnderHere().build();
	}
	
	public FullTestSuite() {
		super();
	}
}
