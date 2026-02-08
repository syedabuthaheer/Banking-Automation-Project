package com.Finaltest;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class MyTestRetry implements IRetryAnalyzer {
	int count = 0;
	int limit = 0;

	@Override
	public boolean retry(ITestResult result) {
		if (count<limit) {
			count++;
			return true;
			
		}
		return false;
	}

}
