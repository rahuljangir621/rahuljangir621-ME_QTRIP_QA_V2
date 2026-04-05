package qtriptest.pages;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import qtriptest.utils.SeleniumUtils;
import static qtriptest.utils.SeleniumUtils.*;
import java.time.Duration;
import java.util.List;
import java.util.UUID;


public class HistoryPage {

    RemoteWebDriver driver;
 


    public HistoryPage(RemoteWebDriver driver){

        this.driver=driver;

       

        PageFactory.initElements(new AjaxElementLocatorFactory(driver,15 ), this);
    }

    @FindBy (css="#reservation-table>tr")
    private List<WebElement> reservationList;

    @FindBy(xpath="//h1[text()='Your Reservations']")
    private WebElement reservationformTitle;

    @FindBy(id="no-reservation-banner")
    private WebElement noReservationBanner;

    @FindBy(xpath="//div[text()='Logout']")
    private WebElement logoutButton;
    
    @FindBy(xpath="//a[text()='Home']")
    private WebElement homeButton;


    // public boolean isReservationPageNavigationSucceded(){

    //     logger("Check for registration navigation");
    //     return 
    //     checkPageTransition(driver,Page_endPoint,reservationformTitle,"Your Reservations");
       
    // }

    public void clickOnHomeButton(){
        clickOnElement(homeButton);
    }

    public void clickOnLogoutButton(){
        clickOnElement(logoutButton);
    }

    public boolean checkTrackIDandCancel() {
        // Loop until no reservations remain
        while (driver.findElements(By.cssSelector(".reservation-row")).size() > 0) {
            // Always re-fetch the list after each iteration
            List<WebElement> reservationList = driver.findElements(By.cssSelector(".reservation-row"));
    
            WebElement record = reservationList.get(0); // cancel the first one
            String transactionId = record.findElement(By.cssSelector("th")).getText().trim();
            SeleniumUtils.logger("Transaction ID is: " + transactionId);
    
            // Click cancel button
            WebElement cancelButton = record.findElement(By.cssSelector(".cancel-button"));
            cancelButton.click();
    
            // Wait for DOM update (instead of immediate refresh)
            SeleniumUtils.waitForElementVisibility(driver, record);
    
            // Optionally refresh if needed, but re-query elements after refresh
            driver.navigate().refresh();
            SeleniumUtils.waitForElementVisibility(driver, noReservationBanner);
        }
        return true;
    }
    }





















