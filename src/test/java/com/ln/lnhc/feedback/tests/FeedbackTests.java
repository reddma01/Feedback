package com.ln.lnhc.feedback.tests;

import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Recordset;
import com.ln.lnhc.fb.pages.FeedbackFormPage;
import com.ln.lnhc.fb.pages.LoginPage;
import com.ln.lnhc.fb.pages.ManageFeedbackPage;
import com.ln.lnhc.fb.utilities.DataRetriever;
import com.ln.lnhc.feedback.base.BaseTest;

import freemarker.log.Logger;

public class FeedbackTests extends BaseTest {

	FeedbackFormPage _feedbackForm = null;

	ManageFeedbackPage _manageForm = null;

	LoginPage _loginPage = null;

	public Map<String, String> data;

	@BeforeMethod
	public void beforeMethod() {
		_feedbackForm = new FeedbackFormPage(driver, logger);
		_manageForm = new ManageFeedbackPage(driver, logger);
		_loginPage = new LoginPage(driver, logger);

	}

	@Test
	public void FeedBackFormTest() throws FilloException {

		Recordset data = DataRetriever.getAllData("Select * from FeedBackData");
		logger.createTestCase("Create User Tests");

		try {
			while (data.next()) {
				SoftAssert sa = new SoftAssert();
				logger.createNode("Creating User " + data.getField("FirstName") + " " + data.getField("LastName"));

				_feedbackForm.setFirstName(data.getField("firstname"));
				_feedbackForm.setLastName(data.getField("lastname"));
				_feedbackForm.setdeaNumber(data.getField("deanumber"));
				_feedbackForm.setPhone(data.getField("phone"));

				_feedbackForm.setAddress(data.getField("address"));
				_feedbackForm.setCity(data.getField("city"));
				_feedbackForm.setState(data.getField("state"));
				_feedbackForm.setZip(data.getField("zip"));

				_feedbackForm.clickaddToQueue();
				_feedbackForm.clicksubmitFeedBack();
				_feedbackForm.clickadmin();

				_manageForm.clickProvider();
				_manageForm.clickPlusBtn();
				_manageForm.clickLookUpBtn();
				_manageForm.clickCreateCallBtn();
				sa.assertTrue(_manageForm.verifyCallResult());
				_feedbackForm.clickusers();

				sa.assertAll();

			}

		} catch (Exception e) {

			e.printStackTrace();
			
		}

	}

}
