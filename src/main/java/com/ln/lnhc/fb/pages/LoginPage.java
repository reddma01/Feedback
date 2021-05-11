package com.ln.lnhc.fb.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.ln.lnhc.fb.base.BasePage;
import com.ln.lnhc.fb.utilities.ExtentReport;



public class LoginPage extends BasePage {

	public LoginPage(WebDriver pDriver,ExtentReport plogger) {
		driver = pDriver;
		logger=plogger;

	}

	By fb_userName = By.id("loginForm:username");

	By fb_password = By.id("loginForm:pass");

	By fb_login_btn = By.xpath("//button[@id='loginForm:loginBtn']");

	public void login(String userName, String pwd) {

		setText(fb_userName, userName);
		setText(fb_password, pwd);
		click(fb_login_btn);
		waitUntillPageCompletedLoading();

	}
}
