
    package qtriptest.tests;

    import static qtriptest.utils.SeleniumUtils.*;
    import  qtriptest.ExtendTestManager;
    import  qtriptest.ExtendReportManager;    
    import qtriptest.DriverSingleton;
import qtriptest.ExtendTestManager;
import org.openqa.selenium.remote.RemoteWebDriver;
    import org.testng.ITestResult;
    import org.testng.annotations.*;
    import java.io.IOException;
    import java.lang.reflect.Method;
    
    public class BaseTest {
    
        protected static RemoteWebDriver driver;
        String browser = "chrome";
        String APP_URL = "https://qtrip-qa-web.labs.crio.do/";
    
        @BeforeSuite(alwaysRun = true)
        public void initTestSuite() throws Exception {
            driver = DriverSingleton.getDriver(browser);
            logPass("🌐 Browser selected: " + browser);
            driver.get(APP_URL);
            logPass("🚀 Application launched: " + APP_URL);
        }
    
        @BeforeMethod(alwaysRun = true)
        public void beforeEachTestMethod(Method method) {
            ExtendTestManager.startTest(method.getName());
        }
    
        @AfterMethod(alwaysRun = true)
        public void afterEachTestMethod(ITestResult iTestResult) throws IOException {
            String testName = iTestResult.getMethod().getMethodName();
        
            if (iTestResult.getStatus() == ITestResult.SUCCESS) {
                logPass("✅ TEST PASSED: " + testName);
                ExtendTestManager.logPass("test case passed " + testName);
            } else if (iTestResult.getStatus() == ITestResult.FAILURE) {
                logPass("❌ TEST FAILED: " + testName);
                if (driver != null) {
                    String screenshotPath = capture(driver);
                    ExtendTestManager.logErrorWithScreenshot(iTestResult.getThrowable().toString(), screenshotPath);
                } else {
                    ExtendTestManager.logfail("Driver was null, screenshot not captured. Error: "
                                                + iTestResult.getThrowable().toString());
                }
            } else {
                logPass("⚠️ TEST SKIPPED: " + testName);
            }
         }
        

    
        @AfterSuite(alwaysRun = true)
        public void tearDownSuite() { 
            driver.quit();
            
            ExtendTestManager.endTest();
         
        }
    
    }
    