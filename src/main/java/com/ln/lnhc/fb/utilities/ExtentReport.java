package com.ln.lnhc.fb.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.AnalysisStrategy;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReport {

	public static ExtentReports extent;
	public static ExtentSparkReporter spark;
	public static ExtentTest test = null;
	public static ExtentTest node=null;
	public static String reportFile = null;
	public static Config cfg = null;
	public static String fileName = null;
	public static WebDriver driver;

	public ExtentReport(Config cfg2) {
		cfg = cfg2;

		extent = new ExtentReports();

		// create the output folder for the report file if it's not there already
		reportFile = FileUtil.getFilePathname(cfg.getProperty("extent.report.pathname"));
		// System.out.println("reportFile = " + reportFile);
		FileUtil.makeDirectory(FileUtil.getParentDir(reportFile));

		spark = new ExtentSparkReporter(cfg.getProperty("extent.report.pathname"));
		spark.config().setDocumentTitle("Feedback Automation");
		extent.setSystemInfo("Environment", cfg.getProperty("extent.report.environment"));
		extent.setSystemInfo("User Name", Info.getUsername());
		extent.setSystemInfo("Host Name", Info.getComputerName());
		extent.setSystemInfo("OS", Info.getOsName());
		extent.setSystemInfo("HeadLess", cfg.getProperty("headless"));
		extent.setSystemInfo("Environtment", cfg.getProperty("url"));
		if (System.getProperty("browser") != null)
			extent.setSystemInfo("Browser", System.getProperty("browser"));
		else
			extent.setSystemInfo("Browser", cfg.getProperty("browser"));
		extent.setAnalysisStrategy(AnalysisStrategy.CLASS);
		// html.loadXMLConfig("./avent-config.xml");

		extent.attachReporter(spark);
	}

	public ExtentReport(WebDriver pdriver) {
		driver = pdriver;
	}

	/**
	 * close the report and flush it to disk
	 * 
	 * <pre>
	 * {
	 * 	&#64;code
	 * 	Config config = new Config(ITestContext);
	 * 	ExtentReport report = new ExtentReport(config);
	 * 	report.close();
	 * }
	 * </pre>
	 * 
	 */
	public void close() {

		extent.flush();
		// System.out.println("Flush report");

	}

	/**
	 * Create a new test case
	 * 
	 * <pre>
	 * {
	 * 	&#64;code
	 * 	Config config = new Config(ITestContext);
	 * 	ExtentReport report = new ExtentReport(config);
	 * 	report.createTestCase("Test Case Name");
	 * }
	 * </pre>
	 * 
	 * @param name - the name of the test case
	 */
	public void createTestCase(String name) {
		try {
			test = extent.createTest(name);

		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * Create a new test case with Description
	 * 
	 * <pre>
	 * {
	 * 	&#64;code
	 * 	Config config = new Config(ITestContext);
	 * 	ExtentReport report = new ExtentReport(config);
	 * 	report.createTestCase("Test Case Name", "The first test case");
	 * }
	 * </pre>
	 * 
	 * @param name        - the name of the test case
	 * @param description - a description of the test case
	 */
	public void createTestCase(String name, String description) {
		try {
			test = extent.createTest(name, description);
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	/**
	 * Get the current report filename
	 * 
	 * <pre>
	 * {
	 * 	&#64;code
	 * 	Config config = new Config(ITestContext);
	 * 	ExtentReport report = new ExtentReport(config);
	 * 	String p = report.getReportFile();
	 * }
	 * </pre>
	 * 
	 * @return the current report file name
	 */
	public static String getReportFile() {
		return reportFile;
	}

	public void passTestCase(String details) {
		// test.log(Status.PASS, MarkupHelper.createLabel(details, ExtentColor.GREEN));
		test.pass(details);

	}

	public void failTestCase(String details) {
		// test.log(Status.FAIL, MarkupHelper.createLabel(details, ExtentColor.RED));
		test.fail(details);
		try {
			ExtentReport.test.fail("Screenshot -->",
					MediaEntityBuilder.createScreenCaptureFromBase64String(getBase64Screenshot()).build());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void passNode(String details) {
		node.pass(details);
		

	}
	
	public void failNode(String details) {
		node.fail(details);
		try {
			node.fail("Screenshot -->",
					MediaEntityBuilder.createScreenCaptureFromBase64String(getBase64Screenshot()).build());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createNode(String details) {
		node=test.createNode(details);
	}

	public void infoTestCase(String details) {
	//	test.log(Status.INFO, MarkupHelper.createLabel(details, ExtentColor.GREY));
		test.info(details);
		

	}

	public void skipTestCase(String details) {
		test.log(Status.SKIP, MarkupHelper.createLabel(details, ExtentColor.YELLOW));

	}

	public void failTestCase(Throwable throwable) {
		// TODO Auto-generated method stub
		test.log(Status.FAIL, throwable);
	}

	public static String getBase64Screenshot() throws IOException {

		String encodedBase64 = null;
		FileInputStream fileInputStream = null;
		TakesScreenshot screenshot = (TakesScreenshot) driver;
		File source = screenshot.getScreenshotAs(OutputType.FILE);
		String destination = System.getProperty("user.dir") + "\\target/screenshots/" + getcurrentdateandtime()
				+ ".png";
		File finalDestination = new File(destination);
		FileUtils.copyFile(source, finalDestination);

		try {
			fileInputStream = new FileInputStream(finalDestination);
			byte[] bytes = new byte[(int) finalDestination.length()];
			fileInputStream.read(bytes);
			encodedBase64 = new String(Base64.encodeBase64(bytes));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return "data:image/png;base64," + encodedBase64;

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
