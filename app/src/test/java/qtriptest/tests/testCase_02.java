package qtriptest.tests;

import qtriptest.DP;
import qtriptest.DriverSingleton;
import qtriptest.pages.AdventurePage;
import qtriptest.pages.HomePage;
import qtriptest.pages.LoginPage;
import qtriptest.pages.RegisterPage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.annotations.Test.*;
import org.testng.asserts.SoftAssert;
import static qtriptest.utils.SeleniumUtils.*;

public class testCase_02 {

    RemoteWebDriver driver;
    HomePage homepage;
    RegisterPage registerpage;
    LoginPage loginpage;
    AdventurePage adventurePage;
    SoftAssert sa;
    String passwrd = "Rahu@123";
    String username = "s@email.com";

    @BeforeClass
    public void config() throws MalformedURLException {
        // 1. Get the driver instance and assign it to your local 'driver' variable
        driver = DriverSingleton.getDriver();
        
        // 2. SAFETY CHECK: If driver is still null here, the test will fail immediately with a clear message
    
        // 3. Now perform setup
        DriverSingleton.openApp("https://qtrip-qa-web.labs.crio.do/");
        
        // 4. Initialize page objects with the active driver
        homepage = new HomePage(driver);
        registerpage = new RegisterPage(driver);
        loginpage = new LoginPage(driver, registerpage);
        adventurePage = new AdventurePage(driver);
    }    
 
  
    @Test(description = "UserOnBoardFlow", dataProvider = "userDataForTestCase02", dataProviderClass = testCase_02.class,priority = 1,groups={"verify home search"},enabled=true)

    public void TestCase02(String search_items,String categoryValue,String hourValue,String expectedFilterResult ,String unexpectedFilterResult) {
        sa = new SoftAssert();
        logger("navigate to qtrip home page");
        homepage.navigateToHomePage();
        logger("verify that if search valid city");
        homepage.searchCityResult(search_items);
        logger("verify that if search valid place suggestion should show");
        sa.assertTrue(homepage.searchCityResult(search_items).contains(search_items), " if search valid place suggestion not showing");
    
        logger("clicking on suggestion city");
        homepage.selectSearchCityAndNavigate();

        logger("selecting  Category Filters page");
        adventurePage.selectCategoryFilters(categoryValue);

        logger("selecting hours on filter page");
        adventurePage.selectHourFilters(hourValue.trim());
    
      
        System.out.println(adventurePage.countOfAdventureImages(expectedFilterResult));
        sa.assertTrue(adventurePage.countOfAdventureImages(expectedFilterResult), "expected result of places counts not match");
        logger("clear Category AND Hours Category Filters page");
        adventurePage.clearCategoryButton();
        adventurePage.clearHoursButton();
       
        System.out.println(adventurePage.countOfAdventureImages(unexpectedFilterResult));

        sa.assertTrue(adventurePage.countOfAdventureImages(unexpectedFilterResult), "expected result of places counts not match");


        sa.assertAll();
    }
    
    @DataProvider(name= "userDataForTestCase02")
    public Object[][] userDataForTestCase02() throws IOException {
        try{
        DP dataProvider = new DP();
        Object[][] data = dataProvider.dpMethod("TestCase02");
        return data;
        }catch (Exception e){
            System.out.println("err"+e);
            return null;
        }
    }
    

    @AfterClass
    public void teardown() {
        DriverSingleton.unloadDriver(); 
    }

   


    }

