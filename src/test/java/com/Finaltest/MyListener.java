package com.Finaltest;

import java.io.IOException;

import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.Status;

public class MyListener implements ITestListener {
	@Override
	public void onTestStart(ITestResult result) {
		Baseoneprt.test = Baseoneprt.extent.createTest(result.getMethod().getMethodName());
	}

	@Override
	public void onTestSuccess(ITestResult result) {
		Baseoneprt.test.log(Status.PASS, "Test is Passed");
	}

	@Override
	public void onTestFailure(ITestResult result) {
		Baseoneprt.test.log(Status.FAIL, "Test is Failed: " + result.getThrowable());

		try {
			String ErrorLocation = new Baseoneprt().TakemyScreenshot(result.getMethod().getMethodName());
			Baseoneprt.test.addScreenCaptureFromPath(ErrorLocation);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onTestSkipped(ITestResult result) {
		Baseoneprt.test.log(Status.SKIP, "Test is skipped");
	}

	@Override
	public void onStart(ITestContext context) {

	}

	@Override
	public void onFinish(ITestContext context) {

	}

}
