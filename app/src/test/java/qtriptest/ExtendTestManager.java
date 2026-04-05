package qtriptest;

import freemarker.core.ReturnInstruction.Return;
import java.util.HashMap;
import java.util.Map;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus; 

public class ExtendTestManager {

    private static ExtentReports extentReports = ExtendReportManager.getExtentReports();
    private static Map <Object,ExtentTest> extenttestmap = new HashMap<>();

    public static ExtentTest startTest(String testmethodname){
    ExtentTest extenttest = extentReports.startTest(testmethodname);
    extenttestmap.put(Thread.currentThread().getId(),extenttest);
    return extenttest;
    } 
    public static ExtentTest getExtendTest(){
    return extenttestmap.get(Thread.currentThread().getId());
    }


    public static void logInfo(String info){
        getExtendTest().log(LogStatus.INFO, info);
       
        }

    public static void logPass(String message) {
        getExtendTest().log(LogStatus.PASS, message);
    }

    public static void logfail(String info){
     getExtendTest().log(LogStatus.FAIL, info);
         }

    public static void logskip(String info){
        getExtendTest().log(LogStatus.SKIP, info);
                             
               }


    public static void logErrorWithScreenshot(String Error_info, String screenshotPath) {
                // Attaches the screenshot to the failure log
                var test = getExtendTest();
                if (test != null) {
                    test.log(LogStatus.FAIL, Error_info + test.addScreenCapture(screenshotPath));
                } else {
                    // Fallback: Print to console so you don't lose the error info
                    System.out.println("ExtentReport Error: getExtendTest() returned null.");
                    System.out.println("Actual Test Failure: " + Error_info);
                    System.out.println("Screenshot saved at: " + screenshotPath);
                }
            }

public static void endTest() {
    extentReports.endTest(getExtendTest());
    extentReports.flush();

}
}