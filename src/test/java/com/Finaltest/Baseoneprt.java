package com.Finaltest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Baseoneprt {
    
    public static ThreadLocal<WebDriver> tdriver = new ThreadLocal<>();
    public static ExtentReports extent;
    public static ExtentTest test;

    public static WebDriver getDriver() {
        return tdriver.get();
    }

    @BeforeSuite(groups = { "Smoke", "Regression", "Sanity" })
    public void TakeMyReport() {
        new File("C:\\Temp").mkdirs();
        String reportPath = System.getProperty("user.dir") + "\\reports\\mytest.html";
        ExtentSparkReporter report = new ExtentSparkReporter(reportPath);
        report.config().setReportName("Banking Automation Test");
        report.config().setDocumentTitle("Test Result");
        report.config().setTheme(Theme.STANDARD);
        extent = new ExtentReports();
        extent.attachReporter(report);
        extent.setSystemInfo("QA Tester", "Syedabuthaheer");
        extent.setSystemInfo("Environment", "Jenkins Run");
    }

    @Parameters("browser")
    @BeforeMethod(groups = { "Smoke", "Regression", "Sanity" })
    public void Browserlanuch(@Optional("chrome") String browserName) {
        
        WebDriver driver = null;
        try {
            
            if (browserName.equalsIgnoreCase("chrome")) {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--headless=new");
                options.addArguments("--window-size=1920,1080");
                options.addArguments("--disable-gpu");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--remote-allow-origins=*");
                
                // DevTools error fix
                options.addArguments("--remote-debugging-port=9222");
                options.addArguments("--user-data-dir=C:\\Temp\\ChromeProfile"); 

                driver = new ChromeDriver(options);
            } 
            
            
            else if (browserName.equalsIgnoreCase("edge")) {
                WebDriverManager.edgedriver().setup();
                EdgeOptions options = new EdgeOptions();
                options.addArguments("--headless=new");
                options.addArguments("--window-size=1920,1080");
                options.addArguments("--disable-gpu");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--remote-allow-origins=*");
                
                
                options.addArguments("--remote-debugging-port=9222");
                options.addArguments("--user-data-dir=C:\\Temp\\EdgeProfile"); 

                driver = new EdgeDriver(options);
            }
        
            if(driver != null) {
                tdriver.set(driver);
                getDriver().manage().window().maximize();
                getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
                getDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
                getDriver().get("https://parabank.parasoft.com/parabank/index.htm");
            }
            
        } catch (Exception e) {
            System.out.println("CRITICAL ERROR: Browser Launch Failed: " + e.getMessage());
            throw new RuntimeException("Browser Launch Failed: " + e.getMessage());
        }
    }

    @AfterMethod(groups = { "Smoke", "Regression", "Sanity" })
    public void Browserclosed() {
        if (getDriver() != null) {
            getDriver().quit();
            tdriver.remove();
        }
    }

    @AfterSuite(groups = { "Smoke, Regression, Sanity" })
    public void Reportsaved() {
        if (extent != null) {
            extent.flush();
        }
    }

    public String TakemyScreenshot(String testname) throws IOException {
        if (getDriver() == null) return System.getProperty("user.dir") + "\\reports\\no_image.png";
        try {
            String mytime = new SimpleDateFormat("yyyyMMddmmss").format(new Date());
            TakesScreenshot ts = (TakesScreenshot) getDriver();
            File source = ts.getScreenshotAs(OutputType.FILE);
            String folderPath = System.getProperty("user.dir") + "\\Screenshot\\";
            new File(folderPath).mkdirs();
            String location = folderPath + testname + "_" + mytime + ".png";
            FileUtils.copyFile(source, new File(location));
            return location;
        } catch (Exception e) {
            return System.getProperty("user.dir") + "\\reports\\error_image.png";
        }
    }

    public void doLogin(String username, String password) {
        try {
            if (getDriver() != null) {
                getDriver().findElement(By.name("username")).sendKeys(username);
                getDriver().findElement(By.name("password")).sendKeys(password);
                getDriver().findElement(By.xpath("//input[@value='Log In']")).click();
            }
        } catch (Exception e) {
            System.out.println("Login Error: " + e.getMessage());
        }
    }

    public void doRegister(String username, String password) {
        try {
            if (getDriver() != null) {
                getDriver().findElement(By.linkText("Register")).click();
                WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(30));
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("customer.firstName"))).sendKeys("TestUser");
                getDriver().findElement(By.id("customer.lastName")).sendKeys("Lastname");
                getDriver().findElement(By.id("customer.address.street")).sendKeys("123 Street");
                getDriver().findElement(By.id("customer.address.city")).sendKeys("Chennai");
                getDriver().findElement(By.id("customer.address.state")).sendKeys("TamilNadu");
                getDriver().findElement(By.id("customer.address.zipCode")).sendKeys("600001");
                getDriver().findElement(By.id("customer.phoneNumber")).sendKeys("9876543210");
                getDriver().findElement(By.id("customer.ssn")).sendKeys("12345");
                
            
                Random rand = new Random();
                String uniqueUser = username + rand.nextInt(10000); 
                
                getDriver().findElement(By.id("customer.username")).sendKeys(uniqueUser);
                getDriver().findElement(By.id("customer.password")).sendKeys(password);
                getDriver().findElement(By.id("customer.repeatedPassword")).sendKeys(password);
                
                getDriver().findElement(By.xpath("//input[@value='Register']")).click();
            }
        } catch (Exception e) {
            System.out.println("Register Error: " + e.getMessage());
        }
    }
}
