import org.openqa.selenium.*;
import org.openqa.selenium.bidi.module.Network;
import org.openqa.selenium.bidi.network.AddInterceptParameters;
import org.openqa.selenium.bidi.network.InterceptPhase;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import org.hamcrest.Matchers.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class NetworkAuth {

    protected WebDriver driver;
    protected WebDriverWait wait;
    String status = "passed";
    String LT_USERNAME = System.getenv("LT_USERNAME");
    String LT_KEY = System.getenv("LT_ACCESS_KEY");

    @BeforeSuite
    public void setup() throws MalformedURLException {
        FirefoxOptions options = new FirefoxOptions();
        options.setPlatformName("Windows 10");
        options.setBrowserVersion("127");
        HashMap<String, Object> ltOptions = new HashMap<String, Object>();
        ltOptions.put("username", LT_USERNAME);
        ltOptions.put("accessKey",LT_KEY );
        ltOptions.put("build", "BiDi - Log Events");
        ltOptions.put("name", "NetworkAuth");
        ltOptions.put("project", "BiDi");
        ltOptions.put("w3c", true);
        ltOptions.put("plugin", "java-testNG");
        options.setCapability("LT:Options", ltOptions);
        options.setCapability("webSocketUrl", true);
        driver = new RemoteWebDriver(new URL("https://hub.lambdatest.com/wd/hub"), options);
        driver = new Augmenter().augment(driver);
    }

    @Test
    void canContinueWithAuthCredentials() {
        try (Network network = new Network(driver)) {
            network.addIntercept(new AddInterceptParameters(InterceptPhase.AUTH_REQUIRED));
            network.onAuthRequired(
                    responseDetails ->
                            network.continueWithAuth(
                                    responseDetails.getRequest().getRequestId(),
                                    new UsernameAndPassword("admin", "admin")));
            driver.get("https://the-internet.herokuapp.com/basic_auth");
            String successMessage = "Congratulations! You must have the proper credentials.";
            WebElement elementMessage = driver.findElement(By.tagName("p"));
            assertThat(elementMessage.getText(), equalTo(successMessage));
        }
    }

    @AfterTest
    public void teardown() {
        if (driver != null) {
            ((JavascriptExecutor) driver).executeScript("lambda-status=" + status);
            driver.quit();
        }
    }

    @AfterSuite
    public void close() {
        driver.quit();
    }
}
