package qtriptest;

import java.net.MalformedURLException;
import java.net.URL;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DriverSingleton {

    private static final String HUB_URL = "http://localhost:8082/wd/hub"; // ✅ set your Selenium Grid hub URL

    private static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();

    private DriverSingleton (){}

    public static RemoteWebDriver getDriver() throws MalformedURLException {
        if (driver.get() == null) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-gpu");
            driver.set(new RemoteWebDriver(new URL(HUB_URL), options));
            driver.get().manage().window().maximize();
            driver.get().manage().deleteAllCookies();
        }
        return driver.get();
    }
    
    public static void unloadDriver() {
        driver.remove();
    }

    public static void openApp(String url) {
        driver.get().get(url);
    }
}
