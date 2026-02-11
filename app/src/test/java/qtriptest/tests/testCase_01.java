package qtriptest.tests;

import qtriptest.DP;

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

public class testCase_01 {

    RemoteWebDriver driver;
    HomePage homepage;
    RegisterPage registerpage;
    LoginPage loginpage;
    SoftAssert sa;
    String passwrd = "Rahu@123";
    String username = "s@email.com";

    @BeforeClass
    public void config() throws MalformedURLException {

        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(BrowserType.CHROME);
        driver = new RemoteWebDriver(new URL("http://localhost:8082/wd/hub"), capabilities);

        driver.manage().window().maximize();
        System.out.println("createDriver()");

        //using DriverSingleton we can initialize the drive
   
        // driver= DriverSingleton.getDriver("chrome");

        driver.get("https://qtrip-qa-web.labs.crio.do/");
      


        homepage = new HomePage(driver);
        registerpage = new RegisterPage(driver);
        loginpage = new LoginPage(driver, registerpage);

        
    }
    
 
  
    @Test(description = "UserOnBoardFlow", dataProvider = "userDataForTestCase01", dataProviderClass = testCase_01.class,priority = 1,groups={"Login Flow"},enabled=true)
    public void TestCase01(String userName, String password) {
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

     

        logger("Step 8: Perform log out");
        homepage.logoutUser();

        sa.assertAll();
    }
    
    @DataProvider(name= "userDataForTestCase01")
    public Object[][] userDataForTestCase01() throws IOException {
        try{
        DP dataProvider = new DP();
        Object[][] data = dataProvider.dpMethod("TestCase01");
        // System.out.println("DataProvider rows: " + data.length);
        return data;
        //  return new Object[][] { {"demoUser", "demoPass"} };
        }catch (Exception e){
            System.out.println("err"+e);
            return null;
        }
    }
    

    @AfterClass
    public void closeTest() {

        driver.quit();
        pause(2000);
    }



}