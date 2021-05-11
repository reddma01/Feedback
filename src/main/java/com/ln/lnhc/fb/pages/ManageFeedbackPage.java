package com.ln.lnhc.fb.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.ln.lnhc.fb.base.BasePage;
import com.ln.lnhc.fb.utilities.ExtentReport;



public class ManageFeedbackPage extends BasePage {

	public ManageFeedbackPage(WebDriver pDriver,ExtentReport plogger) {
		driver = pDriver;
		logger=plogger;
	}

	public String tableXpath = "//div[@class='ui-datatable-tablewrapper']//table";

	By provider = By.id("feedbackForm:feedbackTable:0:j_idt114");

	By plusBtn = By.id("detailForm:j_idt119");

	By lookUpBtn = By.id("detailForm:lookupProvider");

	By lastName = By.id("feedbackForm:searchCriteriaPanel:searchName");

	By createCallBtn = By.id("addCallForm:addCallBtn");

	By clearBtn = By.xpath("//span[contains(text(),'Clear')]");

	By searchBtn = By.xpath("//span[contains(text(),'Search')]");

	public void setLastName(String name) {
		setText(lastName, name);
	}

	public void clickSearchBtn() {
		click(searchBtn);
		waitUntillPageCompletedLoading();
	}

	public void clickProvider() {
		click(provider);
		waitUntillPageCompletedLoading();
		sleep(3000);

	}

	public String getRecordStatus(String name) {
		WebElement row = findInTable(tableXpath, name);
		List<WebElement> cells = row.findElements(By.tagName("td"));
		return cells.get(7).getText();
	}

	public void clickPlusBtn() {
		click(plusBtn);
	}

	public void clickLookUpBtn() {
		click(lookUpBtn);
	}

	public void clickCreateCallBtn() {

		click(createCallBtn);

	}

	public boolean verifyCallResult() {
		String text = driver
				.findElement(By.xpath("//div[@id='detailForm:messages']//div[@class='ui-messages-info ui-corner-all']"))
				.getText();
	
		if (text.contains("Call created successfully with id")) {
			logger.passNode("Call created" +text);
			return true;
		}
		else
		{
			logger.failNode("Call not created"+text);
			return false;
		}

	}
}