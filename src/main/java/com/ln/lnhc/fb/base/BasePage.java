package com.ln.lnhc.fb.base;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ln.lnhc.fb.utilities.ExtentReport;





public class BasePage {

	public static WebDriver driver;
	public static ExtentReport logger;
	protected WebDriverWait wait;
	JavascriptExecutor jse = (JavascriptExecutor) driver;
	
	

	public String encryptedPassword(String pwd) {

		byte[] encodedPwdBytes = Base64.encodeBase64(pwd.getBytes());
		return new String(encodedPwdBytes);
	}

	public String decryptedPassword(String pwd) {
		String decryptedPassword;

		byte[] decodedPwdBytes = Base64.decodeBase64(pwd.getBytes());

		decryptedPassword = new String(decodedPwdBytes);
		return decryptedPassword;
	}

	

	/**
	 * 
	 * Selects the option at the given index..
	 * 
	 * @param we    Webelement for which value has to be selected
	 * 
	 * @param index index value of the option
	 * 
	 */

	public static void selectDropdownByIndex(By by, int index) {
		try {
			Select sel = new Select(driver.findElement(by));
			sel.selectByIndex(index);
		} catch (Exception e) {
		}
	}

	/**
	 * Selects the option whose "value" attribute matches the specified parameter..
	 * 
	 * @param we    Webelement for which value has to be selected
	 * @param value visible value of the option
	 * 
	 */

	public void selectValueDropDown(By by, String value) {
		try {
			scrollToElement(by);
			Select sel = new Select(driver.findElement(by));
			sel.selectByValue(value);
		} catch (Exception e) {

		}

	}

	/**
	 * Selects the option that displays the text matching the parameter.
	 * 
	 * @param we    Webelement for which value has to be selected
	 * @param value visible value of the option
	 * 
	 */

	public void selectVisibleTextDropDown(By by, String text) {
		try {
			scrollToElement(by);
			Select sel = new Select(driver.findElement(by));
			sel.selectByVisibleText(text);
			waitUntillPageCompletedLoading();
		}

		catch (Exception e) {

		}
	}

	public void selectByValueDropDown(By by, String text) {
		try {
			scrollToElement(by);
			Select sel = new Select(driver.findElement(by));
			sel.selectByValue(text);
			waitUntillPageCompletedLoading();
		}

		catch (Exception e) {

		}
	}

	public void waitForElementAppear(By by, int timeout) {
		(new WebDriverWait(driver, timeout)).until(ExpectedConditions.presenceOfElementLocated(by));
	}

	public void click(By by) {
		wait = new WebDriverWait(driver, 60);

		try {

			scrollToElement(by);
			driver.findElement(by).click();
		} catch (Exception e) {
			System.out.println("Unable to find the Web Element:" + by);
			e.printStackTrace();
		}
		waitUntillPageCompletedLoading();
	}

	public void actionClick(By by) {
		Actions actions = new Actions(driver);
		actions.moveToElement(driver.findElement(by)).click().perform();
		waitUntillPageCompletedLoading();
	}

	/**
	 * <br>
	 * <b>Description:</b> <br>
	 * &emsp; This method scrolls to the Web Element and enters the text value on
	 * the input field box on the page. <br>
	 * <br>
	 * <b>Purpose:</b> <br>
	 * &emsp; To have a common method that sets the focus on a Web Element to enter
	 * the text value.
	 * 
	 * @param we   Web Element to enter the text value
	 * @param text String value that populates the input field box
	 */

	public void setText(By by, String text) {
		try {
			driver.findElement(by).clear();
			driver.findElement(by).sendKeys(text);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void enterTextActionClass(By by, String text) {
		driver.findElement(by).clear();
		Actions builder = new Actions(driver);
		builder.sendKeys(driver.findElement(by), text).perform();
		builder.sendKeys(Keys.ENTER).perform();
	}

	public void scrollToElement(By by) {
		WebElement we = driver.findElement(by);
		Actions builder = new Actions(driver);
		builder.moveToElement(we).build().perform();
		// jse.executeScript("arguments[0].scrollIntoView();", we);

	}

	/**
	 * Scrolls to top of the page
	 * 
	 */

	public void scrollToTop() {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0,0);");

	}

	

	public WebElement findInTable(String tableID, String text) {

		WebElement selectedRow = null;
		WebElement table = null;
		int counter = 0;
		boolean found = false;

		// Instance where same id for two tables (Automatic Batch page)
		if (driver.findElements(By.xpath(tableID)).size() == 2)
			table = driver.findElements(By.xpath(tableID)).get(1);
		else
			table = driver.findElement(By.id(tableID));

		List<WebElement> tableRows = table.findElements(By.tagName("tr"));

		for (WebElement row : tableRows) {
			List<WebElement> cells = row.findElements(By.tagName("td"));

			for (WebElement cell : cells) {
				if (cell.getText().equals(text)) {
					selectedRow = row;
					found = true;
					return row;
				}
			}

		}
		if (!found)
			System.out.println("Element in table not found for text: " + text);
		return null;

	}

	
	public void sleep(long milis) {
		try {
			Thread.sleep(milis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public void waitUntillPageCompletedLoading() {
		new WebDriverWait(driver ,60).until(webDriver -> ((JavascriptExecutor) webDriver)
				.executeScript("return(document.readyState == 'complete' && jQuery.active == 0)"));

		sleep(100);
	}

	public void waitForElementToDisappear(By by, int timeout) {
		try {
			WebDriverWait wait = new WebDriverWait(driver, 20, 2000);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(by));

		} catch (Exception e) {

		}
	}

	// Return Current date in the given format
	public String getCurrentDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/YYYY");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}
}
