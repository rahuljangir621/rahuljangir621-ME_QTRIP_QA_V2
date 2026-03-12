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
import java.util.ArrayList;
import java.util.UUID;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import static qtriptest.utils.SeleniumUtils.*;

public class testCase_04 {

    static RemoteWebDriver driver;
    HomePage homepage;
    RegisterPage registerpage;
    LoginPage loginpage;
    AdventurePage adventurePage;
    AdventureDetailsPage adventureDetailsPage;
    HistoryPage   historyPage;
    SoftAssert sa;


    @BeforeTest(alwaysRun = true)
    public void BeforeTest() throws MalformedURLException {
        driver = DriverSingleton.getDriver();
        DriverSingleton.openApp("https://qtrip-qa-web.labs.crio.do/");

        homepage = new HomePage(driver);
        registerpage = new RegisterPage(driver);
        loginpage = new LoginPage(driver, registerpage);
        adventurePage = new AdventurePage(driver);
        adventureDetailsPage = new AdventureDetailsPage(driver);
        historyPage = new HistoryPage(driver);

    }

    public static void logStatus(String type, String message, String status) {

        
      

        System.out.println(String.format("%s |  %s  |  %s | %s", String.valueOf(java.time.LocalDateTime.now()), type,
                message, status));

    }

    @Test(dataProvider = "userDataForTestCase04", dataProviderClass = testCase_04.class,groups = "verfiy booking with diffrent data sets", enabled = true, priority = 4,description = "booking and verifying multiple reservations")
    public void TestCase04(String NewUserName, String Password, String dataset1, String dataset2,String dataset3) throws InterruptedException {
        sa = new SoftAssert();
       

        String confirmPassword = Password;
    
        logger("Step 1: Check visibility of register button on home page");
    
        sa.assertTrue(homepage.isRegisterButtonVisible(), "Register button not found");
    
        logger("Step 2: Navigate to the register page");
        homepage.navigateToRegisterPage();
    
        logger("Step 3: Verify registration page is displayed");
        sa.assertTrue(registerpage.isRegisterPageNavigationSucceded(),
                " register page navigation failed");
             
    
        logger("Step 4: Register new user");
        registerpage.onBOardNewUser(NewUserName, Password, confirmPassword, true);
     
        logger("Step 5: Verify user is on login page");
        loginpage.isLoginPageNavigationSucceded();
       
    
        logger("Step 6: Login with latest user");
        loginpage.performLogin(NewUserName, Password, true);
      
    
        logger("Step 7: Veify user is logged in or not");
        sa.assertTrue(homepage.isLogOutButtonVisible(), "User is not logged in");  
      
            String[] datasets = {dataset1, dataset2, dataset3};
        
            for(String data : datasets) {
        
                String[] values = data.split(";");
        
                String city = values[0];
                String adventure = values[1];
                String guestName = values[2];
                String date = values[3];
                String count = values[4];
        
                System.out.println("Running dataset for city: " + city);
        
                homepage.searchCityResult(city);
                homepage.selectSearchCityAndNavigate();
        
                adventurePage.selectAdventure(adventure);
        
                adventureDetailsPage.enterNameAndDateValue(guestName, date);
                adventureDetailsPage.enterPersonCount(count);
        
                adventureDetailsPage.doReservation();
        
                adventureDetailsPage.checkForSuccessfullReservation();
        
                adventureDetailsPage.navigateToReservationHistoryPage();
        
                historyPage.checkTrackIDandCancel();
                historyPage.clickOnHomeButton();
            }
            sa.assertTrue(homepage.isLogOutButtonVisible(),"log out button is not visible on home page");
            homepage.logoutUser();
            
            sa.assertAll();


    }

        @DataProvider(name= "userDataForTestCase04")
public Object[][] userDataForTestCase03() throws IOException {
    try{
    DP dataProvider = new DP();
    Object[][] data = dataProvider.dpMethod("TestCase04");
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
       
       
       
       
       
       
       