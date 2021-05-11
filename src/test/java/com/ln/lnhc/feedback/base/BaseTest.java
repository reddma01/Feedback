package com.ln.lnhc.feedback.base;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import com.aventstack.extentreports.AnalysisStrategy;
import com.ln.lnhc.fb.pages.LoginPage;
import com.ln.lnhc.fb.utilities.Browsers;
import com.ln.lnhc.fb.utilities.Config;
import com.ln.lnhc.fb.utilities.DataRetriever;
import com.ln.lnhc.fb.utilities.ExtentReport;




public class BaseTest {
	protected static WebDriver driver;
	public static final String FLD_ID = "TestCaseName";
	protected static final String PROJECT_NAME = "Feedback";
	public static int failedSteps = 0;
	private String varURL, varUNAME, varPASSWORD, varBROWSER;
	public static ExtentReport logger;
	

	@AfterMethod(alwaysRun = true)
	public void afterClass() {
		// Close the driver if the driver is not closed yet
		if (driver != null) {
			// driver.close();
			driver.quit();
		}

	}

	@BeforeClass
	public void beforeClass() {
		Config cfg = new Config();
		if (System.getProperty("url") != null)
			varURL = System.getProperty("url");
		else
			varURL = cfg.getProperty("url");

		if (System.getProperty("browser") != null)
			varBROWSER = System.getProperty("browser");
		else
			varBROWSER = cfg.getProperty("browser");

		if (System.getProperty("userName") != null)
			varUNAME = System.getProperty("userName");
		else
			varUNAME = cfg.getProperty("userName");

		if (System.getProperty("password") != null)
			varPASSWORD = System.getProperty("password");
		else
			varPASSWORD = cfg.getProperty("password");

		driver = Browsers.getDriver(varBROWSER, varURL, System.getProperty("SysEnv"));
		logger=new ExtentReport(driver);
		LoginPage _loginPage = new LoginPage(driver, logger);
		_loginPage.login(varUNAME, _loginPage.decryptedPassword(varPASSWORD));
		

	}

	@BeforeSuite
	public void beforeSuite() {

	}

	@AfterSuite
	public void afterSuite() {
		// System.out.println("After Suite is run");
	}

	@BeforeTest
	public void beforeTest(ITestContext testContext) {
		// System.out.println("Before test is run");
		Config cfg = new Config();
		String testName = testContext.getName();
		if (ExtentReport.extent == null) {
			// Set path to save Extent reports
			cfg.setProperty("extent.report.pathname", System.getProperty("user.dir") + "\\target/reports/"
					+ PROJECT_NAME + "-" + format() + "-" + testName + "-" + cfg.getProperty("browser") + ".html");

			logger = new ExtentReport(cfg);
			ExtentReport.extent.setAnalysisStrategy(AnalysisStrategy.CLASS);
		}

	}

	@AfterTest
	public void afterTest() {
		// System.out.println("After test is run");
		if (ExtentReport.extent != null)
			logger.close();
	}

	// Used to retreive data from the database
	public String returnData(String rowData, String table) {
		DataRetriever.readExcel();
		Map<String, String> testFilter = new HashMap<>();
		String query = null;
		testFilter.put(FLD_ID, rowData);
		query = DataRetriever.getQuery(table, testFilter);
		return query;
	}

	public String format() {
		Date nowIs = new Date();
		return format(nowIs);
	}

	public static String format(Date date) {
		SimpleDateFormat sm = new SimpleDateFormat("yyyyMMdd-HHmmss");
		return sm.format(date);
	}

	

	public void sleep(long milis) {
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@AfterMethod(alwaysRun = true)
	public void cleanTest(ITestResult result) throws InterruptedException {

		if (result.getStatus() == ITestResult.FAILURE) {
			logger.failTestCase("Error: Failed " + this.getClass().getName());
			logger.failTestCase(result.getThrowable());

		} else if (result.getStatus() == ITestResult.SKIP) {
			logger.createTestCase(result.getName());
			logger.skipTestCase(this.getClass().getName());

		}

	}
	public static String getcurrentdateandtime() {
		String str = null;
		try {
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss:SSS");
			Date date = new Date();
			str = dateFormat.format(date);
			str = str.replace(" ", "").replaceAll("/", "").replaceAll(":", "");
		} catch (Exception e) {

		}
		return str;
	}

}
