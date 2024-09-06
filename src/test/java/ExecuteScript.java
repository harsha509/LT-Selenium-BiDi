
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.bidi.module.Script;
import org.openqa.selenium.bidi.script.EvaluateResult;
import org.openqa.selenium.bidi.script.EvaluateResultSuccess;
import org.openqa.selenium.bidi.script.LocalValue;
import org.openqa.selenium.bidi.script.PrimitiveProtocolValue;
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
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ExecuteScript {
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
        ltOptions.put("name", "ExecuteScript");
        ltOptions.put("project", "BiDi");
        ltOptions.put("w3c", true);
        ltOptions.put("plugin", "java-testNG");
        options.setCapability("LT:Options", ltOptions);
        options.setCapability("webSocketUrl", true);
        driver = new RemoteWebDriver(new URL("https://hub.lambdatest.com/wd/hub"), options);
        driver = new Augmenter().augment(driver);
    }

    @Test
    void canCallFunctionWithThisParameter() {
        String id = driver.getWindowHandle();
        try (Script script = new Script(id, driver)) {
            Map<Object, LocalValue> value = new HashMap<>();
            value.put("some_property", PrimitiveProtocolValue.numberValue(42));
            LocalValue thisParameter = LocalValue.objectValue(value);

            EvaluateResult result =
                    script.callFunctionInBrowsingContext(
                            id,
                            "function(){return this.some_property}",
                            false,
                            Optional.empty(),
                            Optional.of(thisParameter),
                            Optional.empty());

            EvaluateResultSuccess successResult = (EvaluateResultSuccess) result;
            assertThat(successResult.getResult().getType(), equalTo("number"));
            assertThat((Long) successResult.getResult().getValue().get(), equalTo(42L));
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
