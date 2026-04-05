package qtriptest.tests;

import qtriptest.DP;
import qtriptest.ExtendTestManager;
import qtriptest.pages.*;
import qtriptest.utils.SeleniumUtils;
// import qtriptest.utils.ExtentTestManager;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import static qtriptest.utils.SeleniumUtils.*;

public class testCase_04 extends BaseTest {

    RegisterPage registerPage;
    LoginPage loginPage;
    HomePage homePage;
    AdventurePage adventurePage;
    AdventureDetailsPage adventureDetailsPage;
    HistoryPage historyPage;
    SoftAssert softAssert;

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        driver = BaseTest.driver; // ✅ shared driver
        homePage = new HomePage(driver);
        registerPage = new RegisterPage(driver);
        adventurePage = new AdventurePage(driver);
        loginPage = new LoginPage(driver,registerPage);
        adventureDetailsPage = new AdventureDetailsPage(driver);
        historyPage = new HistoryPage(driver);
        softAssert = new SoftAssert();

        SeleniumUtils.logPass("✅ Initialized all page objects for TestCase_04");
        // ExtentTestManager.logInfo("✅ Initialized all page objects for TestCase_04");
    }

    @Test(
        priority = 4,
        description = "Verify multiple bookings across cities and reservation history",
        groups = { "Reliability Flow" },
        dataProvider = "dpMethod",
        dataProviderClass = DP.class,
        enabled = true
    )
    public void TestCase04(
        String NewUserName,
        String Password,
        String dataset1,
        String dataset2,
        String dataset3
    ) {
        try {
            Password = "abc@123";

            String[] data1 = dataset1.split(";");
            String[] data2 = dataset2.split(";");
            String[] data3 = dataset3.split(";");

            // ExtentTestManager.logInfo("STEP-1️⃣: Navigate to HomePage of QTrip");
            logPass("STEP 1️⃣: Navigating to Home Page");
            Assert.assertTrue(homePage.isRegisterButtonVisible(), "Register button not visible on home page");
            logPass("Register button is visible on the Home Page.");

            logPass("STEP 2️⃣: Navigating to Register Page");
            homePage.navigateToRegisterPage();

            logPass("STEP 3️⃣: Verifying Register Page");
            Assert.assertTrue(registerPage.isRegisterPageNavigationSucceded(), "Register Page navigation failed");
            logPass("Successfully navigated to Register Page.");

            // ✅ Step 4: Make email unique while keeping Excel as base
            String uniqueEmail = NewUserName.split("@")[0] + "+" + System.currentTimeMillis() + "@" + NewUserName.split("@")[1];
            logPass("Generated unique email: " + NewUserName);

            logPass("STEP 4️⃣: Registering a new user");
            registerPage.onBOardNewUser(uniqueEmail, Password, Password);
            logPass("User registration successful with email: " + uniqueEmail);
            


            logPass("STEP 5️⃣: Verifying navigation to Login Page");
            Assert.assertTrue(loginPage.isLoginPageNavigationSucceded(), "Login Page navigation failed");
            logPass("Redirected to Login Page successfully.");

            logPass("STEP 6️⃣: Logging in with registered user credentials");
            loginPage.performLogin(uniqueEmail, Password,false);
            logPass("Login attempt made successfully.");

            // ---------- DATASET 1 ----------
            ExtendTestManager.logInfo("STEP-3️⃣: Booking for dataset 1: " + data1[0]);
            performBookingFlow(data1[0], data1[1], data1[2], data1[3], data1[4]);
            ExtendTestManager.logPass("✅ Dataset 1 booking successful");
//happy coding
            // ---------- DATASET 2 ----------
            ExtendTestManager.logInfo("STEP-4️⃣: Booking for dataset 2: " + data2[0]);
            performBookingFlow(data2[0], data2[1], data2[2], data2[3], data2[4]);
            ExtendTestManager.logPass("✅ Dataset 2 booking successful");

            // ---------- DATASET 3 ----------
            ExtendTestManager.logInfo("STEP-5️⃣: Booking for dataset 3: " + data3[0]);
            performBookingFlow(data3[0], data3[1], data3[2], data3[3], data3[4]);
            ExtendTestManager.logPass("✅ Dataset 3 booking successful");

            // ---------- VERIFY HISTORY ----------
            ExtendTestManager.logInfo("STEP-6️⃣: Navigating to History page");
            adventureDetailsPage.navigateToReservationHistoryPage();
            ExtendTestManager.logPass("✅ Navigated to history page successfully");

            ExtendTestManager.logInfo("STEP-7️⃣: Cancelling all reservations");
            boolean status = historyPage.checkTrackIDandCancel();
            softAssert.assertTrue(status, "Cancel all reservations failed");
            ExtendTestManager.logPass("✅ All reservations cancelled successfully");

            ExtendTestManager.logInfo("STEP-8️⃣: Logging out of application");
            homePage.logoutUser();
            softAssert.assertTrue(homePage.isRegisterButtonVisible(), "Logout failed");
            ExtendTestManager.logPass("✅ Logout successful");

            softAssert.assertAll();
            ExtendTestManager.logPass("🎯 TestCase04 passed successfully");

        } catch (Exception e) {
            e.printStackTrace();
            ExtendTestManager.logfail("❌ TestCase04 failed: " + e.getMessage());
            softAssert.fail("Test failed: " + e.getMessage());
            softAssert.assertAll();
        }
    }

    private void performBookingFlow(String city, String adventure, String guest, String date, String count) {
        try {
            homePage.navigateToHomePage();
            String result = homePage.searchCityResult(city);
          
            softAssert.assertTrue(result.contains(city),  "City search mismatch");
            homePage.selectSearchCityAndNavigate();
            boolean status = adventurePage.verfiy_navigate_to_currect_city(city);
            
            softAssert.assertTrue(status, "Incorrect city navigation");
            ExtendTestManager.logPass("✅ Navigated to " + city + " successfully");

            ExtendTestManager.logInfo("STEP-4️⃣: Search and select adventure");
            adventurePage.selectAdventure(adventure);
            

            result = adventureDetailsPage.verfiy_navigate_to_adv_name();

            softAssert.assertTrue(adventure.toLowerCase().contains(result),
    "Adventure name mismatch expected substring [" +result+ "] but found [" + adventure + "]");

            // softAssert.assertEquals(result, adventure.toLowerCase(), "Adventure name mismatch");

            ExtendTestManager.logPass("✅ Selected adventure: " + adventure);

            ExtendTestManager.logInfo("STEP-5️⃣: Verify details page and book adventure");
            adventureDetailsPage.enterNameAndDateValue(guest, date);
            adventureDetailsPage.enterPersonCount(count);
            adventureDetailsPage.doReservation();

            softAssert.assertTrue(adventureDetailsPage.checkForSuccessfullReservation(), "Booking failed");
        } catch (Exception e) {
            ExtendTestManager.logfail("⚠️ performBookingFlow failed: " + e.getMessage());
            ExtendTestManager.logfail("⚠️ Booking flow failed for city " + city + ": " + e.getMessage());
            softAssert.fail("Booking flow failed: " + e.getMessage());
        }
    }
}
