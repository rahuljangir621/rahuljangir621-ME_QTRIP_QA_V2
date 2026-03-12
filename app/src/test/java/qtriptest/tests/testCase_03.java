package qtriptest.tests;

import qtriptest.DP;
import qtriptest.DriverSingleton;
import qtriptest.pages.AdventureDetailsPage;
import qtriptest.pages.AdventurePage;
import qtriptest.pages.HistoryPage;
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

public class testCase_03 {

    RemoteWebDriver driver;
    HomePage homepage;
    RegisterPage registerpage;
    LoginPage loginpage;
    AdventurePage adventurePage;
    AdventureDetailsPage adventureDetailsPage;
    HistoryPage   historyPage;
    SoftAssert sa;
    String passwrd = "Rahu@123";
    String username = "s@email.com";

    @BeforeClass
    public void config() throws MalformedURLException {

        driver = DriverSingleton.getDriver();
        DriverSingleton.openApp("https://qtrip-qa-web.labs.crio.do/");

        homepage = new HomePage(driver);
        registerpage = new RegisterPage(driver);
        loginpage = new LoginPage(driver, registerpage);
        adventurePage = new AdventurePage(driver);
        adventureDetailsPage = new AdventureDetailsPage(driver);
        historyPage = new HistoryPage(driver);
        
    }
  
    @Test(description = "UserOnBoardFlow", dataProvider = "userDataForTestCase03",groups = "booking a ride", dataProviderClass = testCase_03.class,priority = 1,enabled=true)
    public void TestCase03(String userName, String password,String search_items,String adventure,String gustname,String date,String count) {
    sa = new SoftAssert();
       
    String confirmPassword = password;

    logger("Step 1: Check visibility of register button on home page");

    sa.assertTrue(homepage.isRegisterButtonVisible(), "Register button not found");

    logger("Step 2: Navigate to the register page");
    homepage.navigateToRegisterPage();

    logger("Step 3: Verify registration page is displayed");
    sa.assertTrue(registerpage.isRegisterPageNavigationSucceded(),
            " register page navigation failed");
         

    logger("Step 4: Register new user");
    registerpage.onBOardNewUser(userName, password, confirmPassword, true);
 
    logger("Step 5: Verify user is on login page");
    loginpage.isLoginPageNavigationSucceded();
   

    logger("Step 6: Login with latest user");
    loginpage.performLogin(userName, password, true);
  

    logger("Step 7: Veify user is logged in or not");
    sa.assertTrue(homepage.isLogOutButtonVisible(), "User is not logged in");  
    
    homepage.searchCityResult(search_items);
    logger("verify that if search valid place suggestion should show");
    sa.assertTrue(homepage.searchCityResult(search_items).contains(search_items), " if search valid place suggestion not showing");

    logger("clicking on suggestion city");
    homepage.selectSearchCityAndNavigate();

    logger("clicking on advanture place");
    adventurePage.selectAdventure(adventure);   

    logger("entering gustname and time and date for booking");
    adventureDetailsPage.enterNameAndDateValue(gustname,date);

    logger("entering pecenger count for booking");
    adventureDetailsPage.enterPersonCount(count);

    logger("clicking on confirm booking buttn");
    try {
        adventureDetailsPage.doReservation();
    } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        System.out.println(e);
    }
    logger("verifying that Reservation is booked successfully");
    sa.assertTrue(adventureDetailsPage.checkForSuccessfullReservation(), "adventure booking is not booked");


    logger("navigate on history page");
    adventureDetailsPage.navigateToReservationHistoryPage();

    logger("verifying tranction number and verfiying resversion is canceled successfully");
    historyPage.checkTrackIDandCancel();
    sa.assertTrue( historyPage.checkTrackIDandCancel(),"verfiying resversion is not canceled successfully");

    logger("perfom logout opration");
    historyPage.clickOnLogoutButton();

    sa.assertAll();
}

@DataProvider(name= "userDataForTestCase03")
public Object[][] userDataForTestCase03() throws IOException {
    try{
    DP dataProvider = new DP();
    Object[][] data = dataProvider.dpMethod("TestCase03");
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