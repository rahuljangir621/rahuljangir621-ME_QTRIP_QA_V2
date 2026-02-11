package qtriptest.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;
import static qtriptest.utils.SeleniumUtils.*;
import java.util.UUID;

public class RegisterPage {

    RemoteWebDriver driver;
    private static final String Page_endPoint="/pages/register/";
    protected String dynamicUser;

    public RegisterPage(RemoteWebDriver driver){

        this.driver=driver;

        PageFactory.initElements(new AjaxElementLocatorFactory(driver,15 ), this);
    }

    @FindBy(css=".formtitle:nth-child(1)")
    private WebElement registrationformTitle;

    @FindBy(name="email")
    private WebElement userEmailTextBox;

    @FindBy(name="password")
    private WebElement passwordTextBox;

    @FindBy(name="confirmpassword")
    private WebElement confirmPasswordTextBox;

    @FindBy(xpath="//button[text()='Register Now']")
    private WebElement registerNowButton;

    public boolean isRegisterPageNavigationSucceded(){

        logger("Check for registration navigation");
        return 
        checkPageTransition(driver,Page_endPoint,registrationformTitle,"Register");
       
    }


    public void onBOardNewUser(String email, String password, String confirmPassword, boolean isUserDynamic){

        logger("Registering a new user with "+ email);

        if(isUserDynamic){
            email=String.format("komal_%s@email.com",UUID.randomUUID());
            this.dynamicUser=email;
        }
        enterText(userEmailTextBox, email);
        enterText(passwordTextBox, password);
        enterText(confirmPasswordTextBox, confirmPassword);
        clickOnElement(registerNowButton);
        pause(3000);


    }

}