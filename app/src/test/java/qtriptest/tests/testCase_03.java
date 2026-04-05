package qtriptest.tests;

import qtriptest.DP;
import qtriptest.ExtendTestManager;
import qtriptest.pages.AdventureDetailsPage;
import qtriptest.pages.AdventurePage;
import qtriptest.pages.HistoryPage;
import qtriptest.pages.HomePage;
import qtriptest.pages.LoginPage;
import qtriptest.pages.RegisterPage;
import qtriptest.utils.SeleniumUtils;
import static qtriptest.utils.SeleniumUtils.logPass;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class testCase_03 extends BaseTest {

    RegisterPage registerPage;
    LoginPage loginPage;
    HomePage homePage;
    AdventurePage adventurePage;
    AdventureDetailsPage adventureDetailsPage;
    SoftAssert softAssert;
    HistoryPage historyPage;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        driver = BaseTest.driver;
        homePage = new HomePage(driver);
        registerPage = new RegisterPage(driver);
        adventurePage = new AdventurePage(driver);
        loginPage = new LoginPage(driver,registerPage);
        adventureDetailsPage = new AdventureDetailsPage(driver);
         historyPage = new HistoryPage(driver);
        softAssert = new SoftAssert();

        SeleniumUtils.logPass("✅ Initialized all page objects for TestCase_03");
        ExtendTestManager.logInfo("✅ Initialized all page objects for TestCase_03");
    }

    @Test(
        priority = 3,
        description = "Verify adventure booking and cancellation flow",
        groups = { "Booking and Cancellation Flow" },
        dataProvider = "dpMethod",
        dataProviderClass = DP.class,
        enabled = true
    )
    public void TestCase03(
        String NewUserName,
        String Password,
        String SearchCity,
        String AdventureName,
        String GuestName,
        String Date,
        String count
    ) {
        try {
            ExtendTestManager.logInfo("STEP-1️⃣: Navigate to QTrip Home Page");
            Assert.assertTrue(homePage.isRegisterButtonVisible(), "Register button not visible on home page");
            ExtendTestManager.logPass("✅ Home Page loaded successfully");

          
            homePage.navigateToRegisterPage();

            ExtendTestManager.logInfo("STEP 3️⃣: Verifying Register Page");
            Assert.assertTrue(registerPage.isRegisterPageNavigationSucceded(), "Register Page navigation failed");
            ExtendTestManager.logPass("Successfully navigated to Register Page.");

            // ✅ Step 4: Make email unique while keeping Excel as base
            String uniqueEmail = NewUserName.split("@")[0] + "+" + System.currentTimeMillis() + "@" + NewUserName.split("@")[1];
            logPass("Generated unique email: " + NewUserName);

            ExtendTestManager.logInfo("STEP 4️⃣: Registering a new user");
            registerPage.onBOardNewUser(uniqueEmail, Password, Password);
            ExtendTestManager.logPass("User registration successful with email: " + NewUserName);
    
            Assert.assertTrue(loginPage.isLoginPageNavigationSucceded(), "Login Page navigation failed");
           
           ExtendTestManager.logInfo("STEP 6️⃣: Logging in with registered user credentials");
            loginPage.performLogin(uniqueEmail, Password,false);
            ExtendTestManager.logPass("Login attempt made successfully.");


            String result = homePage.searchCityResult(SearchCity);
            softAssert.assertEquals(result, SearchCity, "City search mismatch");
            homePage.selectSearchCityAndNavigate();
            boolean status = adventurePage.verfiy_navigate_to_currect_city(SearchCity);
            softAssert.assertTrue(status, "Incorrect city navigation");
            ExtendTestManager.logPass("✅ Navigated to " + SearchCity + " successfully");

            ExtendTestManager.logInfo("STEP-4️⃣: Search and select adventure");
            adventurePage.selectAdventure(AdventureName);
            softAssert.assertEquals(adventureDetailsPage.verfiy_navigate_to_adv_name(), AdventureName.toLowerCase(), "Adventure name mismatch");

            ExtendTestManager.logPass("✅ Selected adventure: " + AdventureName);

            ExtendTestManager.logInfo("STEP-5️⃣: Verify details page and book adventure");
            adventureDetailsPage.enterNameAndDateValue(GuestName, Date);
            adventureDetailsPage.enterPersonCount(count);
            adventureDetailsPage.doReservation();

            softAssert.assertTrue(adventureDetailsPage.checkForSuccessfullReservation(), "Booking failed");
            ExtendTestManager.logPass("✅ Adventure booked successfully for guest: " + GuestName);

            ExtendTestManager.logInfo("STEP-6️⃣: Cancel booking from history page");
            adventureDetailsPage.navigateToReservationHistoryPage();
            status =  historyPage.checkTrackIDandCancel();

            softAssert.assertTrue(status, "Booking cancellation failed");
            ExtendTestManager.logPass("✅ Booking cancelled successfully");

            ExtendTestManager.logInfo("STEP-7️⃣: Logout after completion");
            historyPage.clickOnLogoutButton();
            softAssert.assertTrue(homePage.isRegisterButtonVisible(), "Logout failed");
            ExtendTestManager.logPass("✅ Logout successful");

            softAssert.assertAll();
            ExtendTestManager.logPass("🎯 TestCase03 passed successfully");

        } catch (Exception e) {
            e.printStackTrace();
            SeleniumUtils.logPass("❌ TestCase03 failed due to exception: " + e.getMessage());
            ExtendTestManager.logfail("❌ TestCase03 failed due to: " + e.getMessage());
            softAssert.fail("Test failed due to exception: " + e.getMessage());
            softAssert.assertAll();
        }
    }
}
