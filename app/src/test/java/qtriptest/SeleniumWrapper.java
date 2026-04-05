package qtriptest;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import qtriptest.utils.SeleniumUtils;

public class SeleniumWrapper {

    // ✅ Safe Click — includes scroll, existence check, retry
    public static boolean click(WebElement elementToClick, RemoteWebDriver driver) {
        try {
            if (elementToClick == null) return false;
            scrollIntoView(elementToClick, driver);

            new WebDriverWait(driver, 10)
                    .until(ExpectedConditions.elementToBeClickable(elementToClick));

            elementToClick.click();
            SeleniumUtils.logPass("✅ Clicked element: " + elementToClick.toString());
            SeleniumUtils.waitForElementVisibility(driver,elementToClick);
            return true;
        } catch (Exception e) {
            SeleniumUtils.logPass("⚠️ Click failed: " + e.getMessage());
            return false;
        }
    }

    // ✅ Safe SendKeys — clears box and types
    public static boolean sendKeys(WebElement inputBox, String keysToSend, RemoteWebDriver driver) {
        try {
            if (inputBox == null) return false;
            scrollIntoView(inputBox, driver);

            new WebDriverWait(driver, 10)
                    .until(ExpectedConditions.visibilityOf(inputBox));

            inputBox.clear();
            inputBox.sendKeys(keysToSend);
            SeleniumUtils.logPass("⌨️ Sent keys: " + keysToSend);
            SeleniumUtils.waitForElementVisibility(driver,inputBox);
            return true;
        } catch (Exception e) {
            SeleniumUtils.logPass("⚠️ sendKeys failed: " + e.getMessage());
            return false;
        }
    }

    // ✅ Safe navigation — only if URL differs
    public static boolean navigate(RemoteWebDriver driver, String url) {
        try {
            if (!driver.getCurrentUrl().equals(url)) {
                driver.get(url);
                SeleniumUtils.logPass("🌍 Navigated to: " + url);
                
            }
            return true;
        } catch (Exception e) {
            SeleniumUtils.logPass("⚠️ Navigation failed: " + e.getMessage());
            return false;
        }
    }

    // ✅ Retry-based element lookup
    public static WebElement findElementWithRetry(RemoteWebDriver driver, By by, int retryCount) {
        WebElement element = null;
        int attempts = 0;
        while (attempts < retryCount) {
            try {
                element = new WebDriverWait(driver, 10).until(ExpectedConditions.presenceOfElementLocated(by));
                if (element != null) return element;
            } catch (Exception ignored) {}
            SeleniumUtils.logPass("🔁 Retrying to find: " + by.toString());
            SeleniumUtils.pause(500);
            attempts++;
        }
        SeleniumUtils.logPass("❌ Failed to find element after retries: " + by.toString());
        return null;
    }

    // ✅ Scroll helper
    private static void scrollIntoView(WebElement element, RemoteWebDriver driver) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
        } catch (Exception e) {
            SeleniumUtils.logPass("⚠️ scrollIntoView failed: " + e.getMessage());
        }
    }
}
