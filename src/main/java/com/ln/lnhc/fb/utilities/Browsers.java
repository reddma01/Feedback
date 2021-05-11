package com.ln.lnhc.fb.utilities;

import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;



import io.github.bonigarcia.wdm.WebDriverManager;

public class Browsers {

	public static WebDriver getDriver(String pBrowser, String pUrl, String sysEnv) {
		Config cfg = new Config();
		WebDriver driver = null;
		DesiredCapabilities capabilities = null;
		String downloadFilepath = System.getProperty("user.dir") + System.getProperty("file.separator") + "src"
				+ System.getProperty("file.separator") + "test" + System.getProperty("file.separator") + "resources"
				+ System.getProperty("file.separator") + "Downloads" + System.getProperty("file.separator");

		switch (pBrowser.trim().toLowerCase()) {
		case "firefox":
			WebDriverManager.firefoxdriver().setup();		
			
			FirefoxOptions optionsFF = new FirefoxOptions();			
			
			optionsFF.addArguments("start-maximized");
			optionsFF.addArguments("--window-size=1325x744");
			optionsFF.addArguments("--disable-gpu");
			optionsFF.addArguments("--disable-extensions");
			optionsFF.addArguments("--disable-notifications");
			optionsFF.addArguments("--enable-automation");
			optionsFF.addArguments("--disable-save-password-bubble");
			optionsFF.addArguments("test-type");
			optionsFF.addArguments("test-type=browser");
			optionsFF.addArguments("disable-infobars");
			optionsFF.addArguments("--verbose");
			optionsFF.addArguments("--whitelisted-ips=''");

			optionsFF.setLegacy(true);
			
			if (sysEnv != null) {
				try {
					driver = new RemoteWebDriver(new URL("http://selenium__standalone-firefox:4444/wd/hub"), optionsFF);
					Thread.sleep(8000);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			driver = new FirefoxDriver();
			break;

		case "ie":
			
			InternetExplorerOptions ieoptions = new InternetExplorerOptions();	
			WebDriverManager.iedriver().setup();

			driver = new InternetExplorerDriver(ieoptions);
			break;

		case "edge":

			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			break;

		case "chrome":

			HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
			chromePrefs.put("profile.default_content_settings.popups", 0);
			chromePrefs.put("profile.default_content_setting_values.automatic_downloads", 1);
			chromePrefs.put("download.prompt_for_download", false);// turn off prompting for downloads
			chromePrefs.put("download.directory_upgrade", true);
			chromePrefs.put("download.default_directory", downloadFilepath);
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("prefs", chromePrefs);
			if (cfg.getProperty("headless").contains("true"))
				options.setHeadless(true);
			options.addArguments("--test-type");
			options.addArguments("--disable-extensions"); // to disable browser extension
			options.addArguments("--dns-prefetch-disable");
			options.addArguments("disable-popup-blocking");
			options.addArguments("--disable-gpu");
			options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
			options.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
			WebDriverManager.chromedriver().setup();

			if (sysEnv != null) {
				try {
					driver = new RemoteWebDriver(new URL("http://selenium__standalone-chrome:4444/wd/hub"), options);
					Thread.sleep(8000);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			else
				driver = new ChromeDriver(options);
			break;

		}

		driver.get(pUrl);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		return driver;
	}

}


