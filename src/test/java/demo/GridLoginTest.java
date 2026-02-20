package demo;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

import io.restassured.RestAssured;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.net.URL;

public class GridLoginTest {

    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    @Parameters({"browser", "gridUrl"})
    @BeforeMethod
    public void setUp(String browser, String gridUrl) throws Exception {
        if (browser.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--start-maximized");
            driver.set(new RemoteWebDriver(new URL(gridUrl), options));
        } else if (browser.equalsIgnoreCase("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            driver.set(new RemoteWebDriver(new URL(gridUrl), options));
        } else {
            throw new RuntimeException("Unsupported browser: " + browser);
        }
    }

    @Test
    public void openHomePageAndVerifyTitle() {
        driver.get().get("https://example.com");
        String title = driver.get().getTitle();
        System.out.println("Page title = " + title);
        Assert.assertTrue(title != null && !title.trim().isEmpty(), "Title should not be empty");
    }

    @Test
    public void postest() {
        baseURI = "https://jsonplaceholder.typicode.com";
        RestAssured.useRelaxedHTTPSValidation();

        given()
            .relaxedHTTPSValidation()
            .log().all()
        .when()
            .get("/posts/1")
        .then()
            .log().all()
            .statusCode(200);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        // Capture screenshot on failure
        if (result.getStatus() == ITestResult.FAILURE) {
            ScreenshotUtil.takeScreenshot(driver.get(), result.getName() + "_failed");
        }

        if (driver.get() != null) {
            driver.get().quit();
        }
        driver.remove();
    }
}
