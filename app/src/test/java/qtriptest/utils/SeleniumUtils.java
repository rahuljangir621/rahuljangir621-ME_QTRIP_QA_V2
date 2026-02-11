package qtriptest.utils;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class SeleniumUtils {

    final static String screenshotPath = System.getProperty("user.dir")+
    File.separator+"reports";
    

     public static void clickOnElement(WebElement elementToClick){
         System.out.println("Clicking on "+elementToClick);
      //  waitForElementVisibility(driver, elementToClick);
        
        elementToClick.click();
     }

     public static void enterText(WebElement element, String text){
        clickOnElement(element);
        element.clear();
        element.sendKeys(text);
     }

     public static boolean checkPageTransition(RemoteWebDriver driver, String pageEndpoint, WebElement form,String formTitle){
               
        if(formTitle != null || formTitle != " "){
            return  
                    driver.getCurrentUrl().contains(pageEndpoint) &&
                    form.getText().equals(formTitle);
        }else{
            return
            driver.getCurrentUrl().contains(pageEndpoint);
        } 
            
     }

     public static String capture(RemoteWebDriver driver) throws IOException{
        File sourceFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        File destinationFile = new File(screenshotPath+File.separator+System.currentTimeMillis()+".png");

        String filePath = destinationFile.getAbsolutePath();
        FileUtils.copyFile(sourceFile, destinationFile);
        System.out.println("Screenshot placed in :: "+filePath);
        return filePath;
     }

     public static void waitForElementVisibility(RemoteWebDriver driver, WebElement element){
        try{
                WebDriverWait wait = new WebDriverWait(driver, 15);
                wait.until(ExpectedConditions.visibilityOf(element));
        }catch(WebDriverException we){
                we.printStackTrace();
        }
     }

        public static void pause(long time){
            try{

                Thread.sleep(time);
            }catch(InterruptedException ie){
                    ie.printStackTrace();
            }
        }


        public static void logger(String log){
            long timestamp = System.currentTimeMillis();
            String logMessage = String.format("INFO:: %s :: %s",timestamp, log);
            System.out.println(logMessage);
        }
    
}    

