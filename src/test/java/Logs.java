import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.bidi.module.LogInspector;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class Logs {

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
        ltOptions.put("accessKey", LT_KEY);
        ltOptions.put("build", "BiDi - Log Events");
        ltOptions.put("name", "ListenToConsoleEvents");
        ltOptions.put("project", "BiDi");
        ltOptions.put("w3c", true);
        ltOptions.put("plugin", "java-testNG");
        options.setCapability("LT:Options", ltOptions);
        options.setCapability("webSocketUrl", true);
        driver = new RemoteWebDriver(new URL("https://hub.lambdatest.com/wd/hub"), options);
        driver = new Augmenter().augment(driver);
    }

    @Test
    public void listenToConsoleEvents() {
        try (LogInspector logInspector = new LogInspector(driver)) {
            logInspector.onConsoleEntry(consoleLogEntry -> {
                System.out.println("text: " + consoleLogEntry.getText());
                System.out.println("level: " + consoleLogEntry.getLevel());
            });

            String page = "https://www.selenium.dev/selenium/web/bidi/logEntryAdded.html";
            driver.get(page);
            driver.findElement(By.id("consoleLog")).click();
        }

    }

    @Test
    public void jsExceptions() {
        try (LogInspector logInspector = new LogInspector(driver)) {
            logInspector.onJavaScriptException(logEntry -> {
                System.out.println("text: " + logEntry.getText());
                System.out.println("level: " + logEntry.getLevel());
            });


            String page = "https://www.selenium.dev/selenium/web/bidi/logEntryAdded.html";
            driver.get(page);
            driver.findElement(By.id("jsException")).click();
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
