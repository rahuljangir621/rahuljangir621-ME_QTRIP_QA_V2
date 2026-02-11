package qtriptest.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import static qtriptest.utils.SeleniumUtils.*;

public class LoginPage {

    RemoteWebDriver driver;
    private static final String Page_endPoint="/pages/login/";
    private RegisterPage registerpage;

    public LoginPage(RemoteWebDriver driver, RegisterPage registerPage){

        this.driver=driver;
        this.registerpage=registerPage;
        PageFactory.initElements(new AjaxElementLocatorFactory(driver,15 ), this);
    }

    @FindBy(css=".formtitle:nth-child(1)")
    private WebElement loginFormTitle;

    @FindBy(name="email")
    private WebElement userEmailTextBox;

    @FindBy(name="password")
    private WebElement passwordTextBox;

    @FindBy(xpath="//button[text()='Login to QTrip']")
    private WebElement loginToQtripButton;

    @FindBy(xpath="//a[text()='Login Here']")
    private WebElement loginHereButton;

    public boolean isLoginPageNavigationSucceded(){

        logger("Check for login page navigation");
        return 
        checkPageTransition(driver,Page_endPoint,loginFormTitle,"Login");
          
    }

    public void clickOnLoginHereButton(){
        clickOnElement(loginHereButton);
    }


    public void performLogin(String email, String password, boolean isUserDynamic){

        logger("Login with user "+ email);
        if(isUserDynamic){
            email=this.registerpage.dynamicUser;
        }
        
        enterText(userEmailTextBox, email);
        enterText(passwordTextBox, password);
        clickOnElement(loginToQtripButton);
        pause(2000);

    }





















}