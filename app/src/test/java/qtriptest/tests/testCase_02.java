package qtriptest.tests;

import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import org.openqa.selenium.remote.RemoteWebDriver;
import qtriptest.DP;
import qtriptest.pages.HomePage;
import qtriptest.pages.AdventurePage;
import qtriptest.utils.SeleniumUtils;
import qtriptest.ExtendTestManager;
import qtriptest.ExtendReportManager;


public class testCase_02 extends BaseTest {

    HomePage homePage;
    AdventurePage adventurePage;
    SoftAssert softAssert;
    boolean status;

     @BeforeMethod(alwaysRun = true)
    public void setup() {
        driver = BaseTest.driver; // ✅ Use shared driver from BaseTest
        homePage = new HomePage(driver);
        adventurePage = new AdventurePage(driver);
        softAssert = new SoftAssert();
        SeleniumUtils.logPass("✅ Navigated to QTrip Home Page for TestCase02");
        ExtendTestManager.logInfo("✅ Navigated to QTrip Home Page for TestCase02");
    }

    @Test(
        priority = 2,
        description = "Verify search and filter functionalities on Adventure page",
        groups = { "Search and Filter Flow" },
        dataProvider = "dpMethod",
        dataProviderClass = DP.class,
        enabled = true
    )
    public void TestCase02(
            String cityName,
            String categoryFilter,
            String durationFilter,
            String expectedFilteredResults,
            String expectedUnfilteredResults
    ) {
        try {
            ExtendTestManager.logInfo("STEP 1️⃣: Verify Home Page is loaded");
            softAssert.assertTrue(homePage.isRegisterButtonVisible(), "Home Page not loaded properly");

            ExtendTestManager.logInfo("STEP 2️⃣: Search for invalid city");
            String invalidResult = homePage.searchCityResult("RandomCityXYZ");

            status = invalidResult.contains("No City found");
            System.out.println(status);
            
            softAssert.assertTrue(status, "No match message not shown");

            ExtendTestManager.logInfo("STEP 3️⃣: Search for valid city and verify autocomplete");
            String validResult = homePage.searchCityResult(cityName);
            System.out.println(status);
            softAssert.assertTrue(status, "Autocomplete didn’t show valid city");


            ExtendTestManager.logInfo("STEP 4️⃣: Navigate to city");
            homePage.selectSearchCityAndNavigate();
         
            ExtendTestManager.logInfo("STEP 5️⃣: Verify unfiltered activity count");
            status =  adventurePage.countOfAdventureImages(expectedUnfilteredResults);
            softAssert.assertTrue(
               status,
                "Unfiltered count mismatch for " + cityName
            );
            System.out.println(status);

           
            ExtendTestManager.logInfo("STEP 7️⃣: Apply Category Filter: " + categoryFilter);
            adventurePage.selectCategoryFilters(categoryFilter);
          
             ExtendTestManager.logInfo("STEP 6️⃣: Apply Duration Filter: " + durationFilter);
             adventurePage.selectHourFilters(durationFilter);

             status = adventurePage.countOfAdventureImages(expectedFilteredResults);
             System.out.println(status);
             softAssert.assertTrue(
                 status,
                 "Filtered result count mismatch"
             );

            ExtendTestManager.logInfo("STEP 8️⃣: Clear Filters and verify all records restored");
            adventurePage.clearHoursButton();
            adventurePage.clearCategoryButton();
            status = adventurePage.countOfAdventureImages(expectedUnfilteredResults);
            System.out.println(status);
            softAssert.assertTrue(
                status,
                "Filtered result count mismatch"
            );
            driver.navigate().back();
           
            ExtendTestManager.logPass("✅ TestCase02_SearchAndFilterFlow passed successfully");

        } catch (Exception e) {
            e.printStackTrace();
            ExtendTestManager.logfail("❌ TestCase02 failed due to exception: " + e.getMessage());
            softAssert.fail("❌ TestCase02 failed due to exception: " + e.getMessage());
        } finally {
            softAssert.assertAll();
        }
    }
}
