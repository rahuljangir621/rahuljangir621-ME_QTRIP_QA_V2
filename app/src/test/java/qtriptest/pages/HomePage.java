
package qtriptest.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import static qtriptest.utils.SeleniumUtils.*;
import org.openqa.selenium.support.FindBy;


public class HomePage {

    RemoteWebDriver driver;
    private static final String Page_endPoint="/pages/register/";


    public HomePage(RemoteWebDriver driver){

        this.driver=driver;

        PageFactory.initElements(new AjaxElementLocatorFactory(driver,20 ), this);
    }

    @FindBy(xpath="//div[text()='Logout']")
    private WebElement logoutButton;

    @FindBy(xpath="//a[text()='Login Here']")
    private WebElement loginHereButton;

    @FindBy(xpath="//a[text()='Register']")
    private WebElement registerButton;

    @FindBy(xpath="//a[text()='Home']")
    private WebElement homeButton;   

    @FindBy(id="autocomplete")
    private WebElement searchTextBox;

    @FindBy(css="#results")
    private WebElement autoSuggetionField;
    
    public boolean isRegisterButtonVisible(){

        waitForElementVisibility(driver, registerButton);
        return registerButton.getText().equals("Register");
    }

    public void navigateToRegisterPage(){
        clickOnElement(registerButton);     
    }

    public void navigateToHomePage(){
        clickOnElement(homeButton);     
    }
    public boolean isLogOutButtonVisible(){

        waitForElementVisibility(driver, logoutButton);
        return logoutButton.getText().equals("Logout");
    }

    public void logoutUser(){

        clickOnElement(logoutButton);
    }

    public boolean isHomeButtonVisible(){

        waitForElementVisibility(driver, homeButton);
        return homeButton.getText().equals("Home");
    }

    public String searchCityResult(String city){

        clickOnElement(searchTextBox);
        //autoSuggetionField.clear();
        enterText(searchTextBox, city);
        pause(3000);
        return autoSuggetionField.getText().trim();

    }

    public void selectSearchCityAndNavigate(){

        clickOnElement(autoSuggetionField);

    }















}