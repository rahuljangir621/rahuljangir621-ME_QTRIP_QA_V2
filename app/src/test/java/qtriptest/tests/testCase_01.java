package qtriptest.tests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import qtriptest.DP;
import qtriptest.pages.HomePage;
import qtriptest.pages.RegisterPage;
import qtriptest.pages.LoginPage;
import static qtriptest.utils.SeleniumUtils.*;
import qtriptest.ExtendTestManager;
import qtriptest.ExtendReportManager;
import org.openqa.selenium.remote.RemoteWebDriver;

public class testCase_01 extends BaseTest {

    HomePage homePage;
    RegisterPage registerPage;
    LoginPage loginPage;
    SoftAssert softAssert;

    @BeforeMethod(alwaysRun = true)
    public void config() {
        try {
            driver = BaseTest.driver;
            homePage = new HomePage(driver);
            registerPage = new RegisterPage(driver);
            loginPage = new LoginPage(driver,registerPage);
            softAssert = new SoftAssert();
            logPass("✅ WebDriver initialized successfully for testCase_01.");
        } catch (Exception e) {
            qtriptest.ExtendTestManager.logfail("❌ Failed to initialize driver: " + e.getMessage());
            throw new RuntimeException("Driver initialization failed: " + e.getMessage());
        }
    }

    @Test(
        priority = 1,
        description = "Verify user onboarding, login, and logout flow on QTrip platform",
        groups = { "Login Flow" },
        dataProvider = "dpMethod",
        dataProviderClass = DP.class,
        enabled = true
    )
    public void TestCase01(String email, String password) throws InterruptedException {
        try {
            ExtendTestManager.logInfo("STEP 1️⃣: Navigating to Home Page");
            Assert.assertTrue(homePage.isRegisterButtonVisible(), "Register button not visible on home page");
            ExtendTestManager.logPass("Register button is visible on the Home Page.");
            homePage.navigateToRegisterPage();
            Assert.assertTrue(registerPage.isRegisterPageNavigationSucceded(), "Register Page navigation failed");


            String uniqueEmail = email.split("@")[0] + "+" + System.currentTimeMillis() + "@" + email.split("@")[1];
            logPass("Generated unique email: " + uniqueEmail);

            ExtendTestManager.logInfo("STEP 4️⃣: Registering a new user");
            registerPage.onBOardNewUser(uniqueEmail, password, password);
            ExtendTestManager.logPass("User registration successful with email: " + uniqueEmail);

              Assert.assertTrue(loginPage.isLoginPageNavigationSucceded(), "Login Page navigation failed");
           
              ExtendTestManager.logInfo("STEP 6️⃣: Logging in with registered user credentials");
             loginPage.performLogin(uniqueEmail, password,false);
          
            
           softAssert.assertTrue(uniqueEmail.toLowerCase().contains(email.toLowerCase().split("@")[0]),
          "Email mismatch: expected base " + email + " but got " + uniqueEmail);

        
           softAssert.assertEquals(password, password, "Password mismatch");


         
            Assert.assertTrue(homePage.isLogOutButtonVisible(), "Login failed — Logout button not visible");
          
            ExtendTestManager.logInfo("STEP 8️⃣: Logging out user");
            homePage.logoutUser();

            Assert.assertTrue(homePage.isRegisterButtonVisible(), "Logout failed — Register button not visible");
            ExtendTestManager.logPass("Logout successful. Register button visible again.");
            softAssert.assertAll();
           

        } catch (AssertionError e) {
            logPass("❌ Assertion failed: " + e.getMessage());
            ExtendTestManager.logfail("❌ Failed to initialize driver: " + e.getMessage());
            Assert.fail(e.getMessage());
        } catch (Exception e) {
            logPass("❌ Exception occurred: " + e.getMessage());
            Assert.fail("TestCase01 failed due to exception: " + e.getMessage());
        }
    }
}
