package com.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Mayoclinic {
    WebDriver driver;
    Logger logger = Logger.getLogger(getClass());
    ExtentReports reports;
    ExtentTest test;
    @BeforeTest
    public void before() throws Exception{
        ExtentSparkReporter exsprk = new ExtentSparkReporter("D:\\Software Testing\\Model\\mayoclinic\\logandreport\\report1.html");
        reports = new ExtentReports();
        reports.attachReporter(exsprk);
    }
    @Test
    public void test() throws IOException, InterruptedException
    {
        test = reports.createTest("Test 1", "Mayoclinic");
        PropertyConfigurator.configure("D:\\Software Testing\\Model\\mayoclinic\\mc\\src\\main\\resources\\log4j2.properties");
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        FileInputStream fs = new FileInputStream("C:\\Users\\91852\\Desktop\\Modellab\\mayoclinic\\mclinic.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(fs);
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow row = sheet.getRow(0);
        String url = row.getCell(0).getStringCellValue();
        driver.get(url);
        logger.info("open URL");
        driver.findElement(By.xpath("//*[@id=\"header__content-inner-container\"]/div[1]/ul/li[5]/div[1]/div/button/span/span[1]")).click();
        driver.findElement(By.xpath("//*[@id=\"button-d87139392b\"]/span/span/span[1]/span")).click();
        driver.findElement(By.xpath("//*[@id=\"amounts\"]/label[3]")).click();
        WebElement element = driver.findElement(By.xpath("//*[@id='designation']"));
        Select select = new Select(element);
        select.selectByValue("ccfefdc5-4087-428b-883e-ff9bb13c8fd0");
        logger.info("Designation");
        driver.findElement(By.xpath("//*[@id=\"adfWrapper\"]/fieldset[2]/div[1]/label")).click();
        JavascriptExecutor js = (JavascriptExecutor)driver;
        js.executeScript("window.scrollBy(0,1000)", "");
        Thread.sleep(3000);
        logger.warn("wait for 3 sec");
        element = driver.findElement(By.xpath("//*[@id=\"personalTitle\"]"));
        select = new Select(element);
        select.selectByValue("Mr.");
        driver.findElement(By.xpath("//*[@id=\"personalFirstName\"]")).sendKeys("Tester");
        driver.findElement(By.xpath("//*[@id=\"personalLastName\"]")).sendKeys("TesterLast");
        Thread.sleep(3000);
        element = driver.findElement(By.xpath("//*[@id=\"personalCountry\"]"));
        select = new Select(element);
        select.selectByVisibleText("India");
        logger.warn("wait for 3 sec");
        Thread.sleep(3000);
        element = driver.findElement(By.xpath("//*[@id=\"personalState\"]"));
        select = new Select(element);
        select.selectByValue("NA");
        driver.findElement(By.xpath("//*[@id=\"personalAddress\"]")).sendKeys("M.K. Nagar");
        driver.findElement(By.xpath("//*[@id=\"personalCity\"]")).sendKeys("Karur");
        driver.findElement(By.xpath("//*[@id=\"personalZip\"]")).sendKeys("639006");
        driver.findElement(By.xpath("//*[@id=\"personalPhone\"]")).sendKeys("9999999999");
        driver.findElement(By.xpath("//*[@id=\"personalEmail\"]")).sendKeys("abc@gmail.com");
        driver.findElement(By.xpath("//*[@id=\"adfSubmit\"]")).click();
        Thread.sleep(3000);
        logger.warn("wait for 3 sec");
        logger.info("personal details filled");
        driver.switchTo().frame(0);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='header']/h1")));
        String res = driver.findElement(By.xpath("//*[@id='header']/h1")).getText();
        Assert.assertTrue(res.contains("Complete"));
        logger.info("terminated");
    }
        @AfterMethod
    public void afterTest(ITestResult result) throws Exception{
        if(result.getStatus()==ITestResult.FAILURE)
        {
            test.log(Status.FAIL, "TestCase Failed: "+result.getName());
            test.log(Status.FAIL, "Testcase Failed Reason: "+result.getThrowable());
            File screenshot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            String path = "D:\\Software Testing\\Model\\mayoclinic\\logandreport"+result.getName()+"png";
            FileUtils.copyFile(screenshot,new File(path));
            test.addScreenCaptureFromPath(path);

        }
        else if (result.getStatus()==ITestResult.SUCCESS)
        { test.log(Status.PASS, "Test CAse Passed: "+result.getName());
        }
      else if (result.getStatus()==ITestResult.SKIP)
        { test.log(Status.SKIP, "Test CAse Skipped: "+result.getName());
        }
        reports.flush();
        driver.quit();
    }
}
