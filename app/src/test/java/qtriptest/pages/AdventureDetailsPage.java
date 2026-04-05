package qtriptest.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import static qtriptest.utils.SeleniumUtils.*;
import java.util.Date;


public class AdventureDetailsPage {

    RemoteWebDriver driver;

    public AdventureDetailsPage(RemoteWebDriver driver){

        this.driver=driver;
        PageFactory.initElements(new AjaxElementLocatorFactory(driver,15 ), this);
    }

    @FindBy (xpath="//input[@name='name']")
    private WebElement bookingNameTextBox;

    @FindBy (xpath="//input[@name='date']")
    private WebElement bookingDateTextBox;

    @FindBy (xpath="//input[@name='person']")
    private WebElement peopleCountTextBox;

    @FindBy (className="reserve-button")
    private WebElement reservButton;

    @FindBy (id="reservation-person-cost")
    private WebElement reservationPersonCost;

    @FindBy (id="reservation-cost")
    private WebElement reservationCost;

    @FindBy (id="reserved-banner")
    private WebElement reservationBanner;

    @FindBy(xpath = "//a[text()='Reservations']")
    private WebElement reservationButton;

    @FindBy (id="adventure-name")
    private WebElement adventure_name;
    


    public void enterNameAndDateValue(String name, String date){

        enterText(bookingNameTextBox, name);
        bookingDateTextBox.sendKeys(date);

    }

    public String verfiy_navigate_to_adv_name(){
        System.out.println(adventure_name.getText());

       return adventure_name.getText().toLowerCase();

    }
    public boolean checkReservationCalc(String count){

        peopleCountTextBox.clear();
        peopleCountTextBox.sendKeys(count);
        int perPersonCost=Integer.parseInt(reservationPersonCost.getText().trim().replace("₹","").split("per head")[0]);
        int totalCost=Integer.parseInt(reservationCost.getText().trim().replaceAll("₹",""));

        return (perPersonCost * Integer.parseInt(count) == totalCost);
    }

    public void enterPersonCount(String count){

        waitForElementVisibility(driver, peopleCountTextBox);
        peopleCountTextBox.clear();
        peopleCountTextBox.sendKeys(count);
    }

    public void doReservation()throws InterruptedException{

        waitForElementVisibility(driver, reservButton);
        clickOnElement(reservButton);
        Thread.sleep(3000);
    }

    public boolean checkForSuccessfullReservation(){

        waitForElementVisibility(driver,reservationBanner);
        return reservationBanner.getText().contains("Reservation for this adventure is successful");
    }

    public void navigateToReservationHistoryPage(){

        clickOnElement(reservationButton);
    }



}