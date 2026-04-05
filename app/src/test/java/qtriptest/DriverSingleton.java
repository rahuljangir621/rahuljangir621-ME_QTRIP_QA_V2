package qtriptest;

import java.net.URL;
import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * 
 * Lightweight driver factory that will:
 *  - create RemoteWebDriver with retries
 *  - allow re-creation when quit
 *  - use system property "remote.url" if provided; otherwise fallback to localhost:8082
 */
public class DriverSingleton {
    private static RemoteWebDriver driver = null;

    // Configure via -Dremote.url=http://host:port/wd/hub
    private static final String DEFAULT_GRID_URL = "http://localhost:8082/wd/hub";

    public synchronized static RemoteWebDriver getDriver(String browser) {
        if (driver == null) {
            driver = createRemoteDriverWithRetry(3);
        }
        return driver;
    }

    public synchronized static RemoteWebDriver createRemoteDriverWithRetry(int maxAttempts) {
        int attempts = 0;
        Exception lastEx = null;
        String gridUrl = System.getProperty("remote.url", DEFAULT_GRID_URL);

        while (attempts < maxAttempts) {
            try {
                ChromeOptions options = new ChromeOptions();
                // safe flags for headless remote execution
                // use headless new where supported
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--disable-gpu");
                options.addArguments("--window-size=1920,1080");
                options.addArguments("--remote-allow-origins=*");

                RemoteWebDriver tmp = new RemoteWebDriver(new URL(gridUrl), options);
                tmp.manage().timeouts().implicitlyWait(0, java.util.concurrent.TimeUnit.SECONDS); // prefer explicit waits
                return tmp;
            } catch (Exception e) {
                lastEx = e;
                attempts++;
                System.out.println("DriverSingleton: failed to create driver (attempt " + attempts + "). Retrying in 5s...");
                try { Thread.sleep(5000); } catch (InterruptedException ie) { /* ignore */ }
            }
        }
        throw new RuntimeException("DriverSingleton: could not create RemoteWebDriver after " + maxAttempts + " attempts", lastEx);
    }

    public synchronized static void quitDriver() {
        try {
            if (driver != null) {
                driver.quit();
            }
        } catch (Exception e) {
            System.out.println("DriverSingleton: error while quitting driver: " + e.getMessage());
        } finally {
            driver = null;
        }
    }

    /**
     * Force create new driver (quit existing then create)
     */
    public synchronized static RemoteWebDriver recreateDriver() {
        quitDriver();
        driver = createRemoteDriverWithRetry(3);
        return driver;
    }
}
