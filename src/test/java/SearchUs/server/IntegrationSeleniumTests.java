package SearchUs.server;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Keys;

/**
 * Created by Marc-Antoine on 2015-05-20.
 */

public class IntegrationSeleniumTests{

    private static WebDriver driver;

    @BeforeClass
    public static void initWebDriver() throws IOException {

        System.setProperty("webdriver.chrome.driver", "E:\\Programs\\ChromeDriver\\chromedriver.exe");

        // init driver with GWT Dev Plugin
        driver = new ChromeDriver();
    }

    @AfterClass
    public static void theEnd() {
        driver.quit();
    }

    @Before
    public void before() {
    }

    @After
    public void after() {
    }

    @Test
    public void testGoogle() {

        driver.navigate().to("http://google.com");

        WebElement input = driver.findElement(By.id("lst-ib"));

        Assert.assertNotNull("There shoulde be an input in Google",
                input);

        input.sendKeys("Patate",Keys.ENTER);
        input.submit();
    }

    @Test
    public void testSearchUs() {

        driver.navigate().to("localhost:8888/project.html");

        WebElement input = driver.findElement(By.id("gwt-debug-searchTextBox"));
        WebElement button = driver.findElement(By.id("gwt-debug-sendTextButton"));

        Assert.assertNotNull("There should be an input in home page",
                input);
        Assert.assertNotNull("There should be a button in home page",
                button);

        input.sendKeys("Patate", Keys.ENTER);
        button.click();


    }
}
