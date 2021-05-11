package com.ln.lnhc.fb.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.ln.lnhc.fb.base.BasePage;
import com.ln.lnhc.fb.utilities.ExtentReport;

public class FeedbackFormPage extends BasePage {

	public FeedbackFormPage(WebDriver pDriver, ExtentReport plogger) {
		driver = pDriver;
		logger = plogger;
	}

	By firstName = By.id("feedbackForm:firstName");
	By lastName = By.id("feedbackForm:lastName");
	By deaNumber = By.id("feedbackForm:enclarityDeaNum");
	By address = By.id("feedbackForm:enclarityAddress");
	By city = By.id("feedbackForm:enclarityCity");
	By state = By.id("feedbackForm:enclarityState");
	By zip = By.id("feedbackForm:enclarityZip");
	By phone = By.id("feedbackForm:enclarityPhone");
	By addToQueue = By.id("feedbackForm:addToQueue");
	By submitFeedBack = By.id("feedbackForm:submitFeedback");
	By admin = By.xpath("//a[contains(text(),'Admin')]");
	By users = By.xpath("//a[contains(text(),'Users')]");

	public void setFirstName(String strFirstName) {

		setText(firstName, strFirstName);
	}

	public String getFirstName() {
		return driver.findElement(firstName).getText();
	}

	public void setLastName(String strLastName) {

		setText(lastName, strLastName);

	}

	public String getLastName() {
		return driver.findElement(lastName).getText();
	}

	public void setdeaNumber(String strdeaNumber) {

		setText(deaNumber, strdeaNumber);
	}

	public void setAddress(String straddress) {

		setText(address, straddress);
	}

	public void setCity(String strcity) {

		setText(city, strcity);
	}

	public void setState(String strState) {

		WebElement dropdown = driver.findElement(By.id("feedbackForm:enclarityState"));
		Select sel = new Select(dropdown);
		sel.selectByVisibleText("Nevada");

	}

	public void setZip(String strZip) {

		setText(zip, strZip);
	}

	public void setPhone(String strPhone) {

		setText(phone, strPhone);
	}

	public void clickaddToQueue() {

		click(addToQueue);
	}

	public void clicksubmitFeedBack() {

		click(submitFeedBack);
	}

	public void clickadmin() {

		click(admin);
	}

	public void clickusers() {

		click(users);
		waitUntillPageCompletedLoading();
		sleep(1000);
	}

}
