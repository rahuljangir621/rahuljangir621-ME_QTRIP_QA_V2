package qtriptest;
import java.io.File;
import com.relevantcodes.extentreports.ExtentReports;

public class ExtendReportManager {
    private static ExtentReports extendreport = null;
    static String reportPath = System.getProperty("user.dir") + "/reports/ExtentReport.html";

    private ExtendReportManager(){

    }


    public static synchronized ExtentReports getExtentReports(){
        if(extendreport == null){
            extendreport = new ExtentReports(reportPath, true);
        }
        return extendreport;
    }
}

 
    