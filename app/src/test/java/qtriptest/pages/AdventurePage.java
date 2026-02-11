
package qtriptest.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.asserts.SoftAssert;
import static qtriptest.utils.SeleniumUtils.*;
import java.util.List;

public class AdventurePage {

    RemoteWebDriver driver;
    private static final String Page_endPoint="/pages/login/";
    private RegisterPage registerpage;
    private AdventurePage adventurePage;
    private HomePage homepage;
    private AdventureDetailsPage adventureDPage;
    SoftAssert sa=new SoftAssert();

    public AdventurePage(RemoteWebDriver driver){

        this.driver=driver;
        PageFactory.initElements(new AjaxElementLocatorFactory(driver,15 ), this);
    }

    @FindBy(id="duration-select")
    private WebElement filterHours;

    @FindBy(id="category-select")
    private WebElement categoryFilter;

    @FindBy(id="search-adventures")
    private WebElement adventureFilter;

    @FindBy(xpath="//div[@onclick='clearCategory(event)']")
    private WebElement categoryclearButton;

    @FindBy(xpath="//div[@onclick='clearDuration(event)']")
    private WebElement hourclearButton;

    @FindBy(css=".activity-card")
    private List<WebElement> AdventureactivityCardList ;

    public void selectCategoryFilters(String categoryValue){

        clickOnElement(categoryFilter);
        Select select=new Select(categoryFilter);
        select.selectByVisibleText(categoryValue.trim());

    }

    public void selectHourFilters(String hourValue){

        clickOnElement(filterHours);
        Select select=new Select(filterHours);
        select.selectByVisibleText(hourValue.trim());

    }

    public void selectAdventureFilter(String adventureValue ){

        waitForElementVisibility(driver,adventureFilter);
        clickOnElement(adventureFilter);
        adventureFilter.clear();
        enterText(adventureFilter, adventureValue);
        
    }

    public boolean countOfAdventureImages(String expectedFilterResult){

        return AdventureactivityCardList.size()==Integer.parseInt(expectedFilterResult);
      
    }

    public boolean checkForRightCityAdventure(String city){

        return driver.getCurrentUrl().split("\\?city=")[1].equalsIgnoreCase(city);
    }

    public boolean checkDurationOnActivityCard(String hourValue){

        int lowerDuration=Integer.parseInt(hourValue.split("-")[0].trim());
        int higherDuration=Integer.parseInt(hourValue.split("-")[1].replace("Hours","").trim());

        boolean isDurationInRange=false;

        for(WebElement activity : AdventureactivityCardList){

            String actualDurationOnActivity=
            activity.findElement(By.xpath("//*[normalize-space(text())='Duration']/following-sibling::p[1]")).getText().replace("Hours","").trim();

            int actualDuration=Integer.parseInt(actualDurationOnActivity);

            if(actualDuration>=lowerDuration && actualDuration<=higherDuration){
                return isDurationInRange=true;
            }

            if(!isDurationInRange){

                return isDurationInRange;
            }

            return isDurationInRange;

        }

        return true;
    }

    public void clearCategoryButton(){

        clickOnElement(categoryclearButton);

    }

    public void clearHoursButton(){

        clickOnElement(hourclearButton);

    }

    public void selectAdventure(String adventure){

        for(WebElement specificAdventure: AdventureactivityCardList){

            WebElement adventureToSelect=specificAdventure.findElement(By.xpath("//h5[normalize-space()='"+adventure+"']"));
            String adventureName=adventureToSelect.getText().trim();

            if(adventureName.equalsIgnoreCase(adventure)){

                adventureToSelect.click();
            }


        }
    }

    public void bookAdventure(String[] dataset) throws InterruptedException{

        String city=dataset[0];
        String adventure=dataset[1];
        String name=dataset[2];
        String date=dataset[3];
        String personCount=dataset[4];

        homepage.searchCityResult(city);
        homepage.selectSearchCityAndNavigate();

        logger("Step 5: Select adventure from adventure drop down");

        adventurePage.selectAdventureFilter(adventure);

        pause(2000);
        
        adventurePage.selectAdventure(adventure);

        logger("Step 6: Enter name and date for booking");

        adventureDPage.enterNameAndDateValue(name, date);
        sa.assertTrue(adventureDPage.checkForSuccessfullReservation(),"Reservation is not happened");

        adventureDPage.doReservation();


    }
















    


}