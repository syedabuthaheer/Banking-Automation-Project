package com.Finaltest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class Baseoneprt {
	public static ThreadLocal<WebDriver> tdriver = new ThreadLocal<>();
	public static ExtentReports extent;
	public static ExtentTest test;

	public static WebDriver getDriver() {
		return tdriver.get();
	}

	@BeforeSuite(groups = { "Smoke", "Regression", "Sanity" })
	public void TakeMyReport() {
		ExtentSparkReporter report = new ExtentSparkReporter(
				"C:\\Users\\Admin\\eclipse-workspace\\Cumcumber\\reports\\mytest.html");
		report.config().setReportName("My Automation Test");
		report.config().setDocumentTitle("My Test Result");
		report.config().setTheme(Theme.STANDARD);
		extent = new ExtentReports();
		extent.attachReporter(report);
		extent.setSystemInfo("QA Tester", "Syedabuthaheer");

	}

	@Parameters("browser")
	@BeforeMethod(groups = { "Smoke", "Regression", "Sanity" })
	public void Browserlanuch(@Optional("chrome")String browserName) {
		try {
        if (browser.equalsIgnoreCase("chrome")) {
            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--remote-allow-origins=*");
            driver = new ChromeDriver(options);
        } else if (browser.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--remote-allow-origins=*");
            driver = new EdgeDriver(options);
        }
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(20));
        driver.get("https://parabank.parasoft.com/parabank/index.htm");

    } catch (Exception e) {
        System.out.println("Browserlaungh Error " + e.getMessage());
    }
	}

	@AfterMethod(groups = { "Smoke", "Regression", "Sanity" })
	public void Browserclosed() {
		if (getDriver() != null) {
			getDriver().quit();

		}

	}

	@AfterSuite(groups = { "Smoke, Regression, Sanity" })
	public void Reportsaved() {
		if (extent != null) {
			extent.flush();

		}

	}

	public String TakemyScreenshot(String testname) throws IOException {
		String mytime = new SimpleDateFormat("yyyyMMddmmss").format(new Date());
		TakesScreenshot ts = (TakesScreenshot) getDriver();
		File source = ts.getScreenshotAs(OutputType.FILE);
		String location = System.getProperty("user.dir") + "/Screenshot" + testname + "_" + mytime + ".png";
		FileUtils.copyFile(source, new File(location));
		return location;

	}
	public void doLogin(String username, String password) {
		if(getDriver().findElements(By.name("username")).size() > 0) {
            getDriver().findElement(By.name("username")).sendKeys(username);
            getDriver().findElement(By.name("password")).sendKeys(password);
            getDriver().findElement(By.xpath("//input[@value='Log In']")).click();
        }
	}
	public void doRegister(String username, String password) {
		if (getDriver().findElements(By.linkText("Register")).size() > 0) {
			getDriver().findElement(By.linkText("Register")).click();
			WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("customer.firstName"))).sendKeys("TestUser");
			getDriver().findElement(By.id("customer.lastName")).sendKeys("Lastname");
			getDriver().findElement(By.id("customer.address.street")).sendKeys("123 Street");
			getDriver().findElement(By.id("customer.address.city")).sendKeys("Chennai");
			getDriver().findElement(By.id("customer.address.state")).sendKeys("TamilNadu");
			getDriver().findElement(By.id("customer.address.zipCode")).sendKeys("600001");
			getDriver().findElement(By.id("customer.phoneNumber")).sendKeys("9876543210");
			getDriver().findElement(By.id("customer.ssn")).sendKeys("12345");
			getDriver().findElement(By.id("customer.username")).sendKeys(username);
			getDriver().findElement(By.id("customer.password")).sendKeys(password);
			getDriver().findElement(By.id("customer.repeatedPassword")).sendKeys(password);
			getDriver().findElement(By.xpath("//input[@value='Register']")).click();
		}
	}

}
