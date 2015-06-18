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
import org.openqa.selenium.Keys;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by Marc-Antoine on 2015-05-20.
 */

public class IntegrationSeleniumTests{

    private static WebDriver driver;

    private static boolean isOsWindows = false;

    @BeforeClass
    public static void initWebDriver() throws IOException {
        isOsWindows= System.getProperty("os.name").toLowerCase().contains("win");
        if(isOsWindows){
            System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\selenium\\drivers\\chromedriver.exe");
            driver = new ChromeDriver();
        }
        else
        {
            driver = new HtmlUnitDriver();
        }

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
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

    private void NavigateDriverToPage(String url){
        if(isOsWindows){
            driver.navigate().to(url);
        }
        else{
            driver.get(url);
        }
    }

    @Test
    public void testGoogle() {

        NavigateDriverToPage("http://google.com");

        WebElement input = driver.findElement(By.id("lst-ib"));

        Assert.assertNotNull("There should be an input in Google",
                input);

        input.sendKeys("Patate",Keys.ENTER);
        input.submit();
    }

    private void OpenSearchUs(){
        // TODO : FIX ME : Do not hard code localhost (Find a way to get ip of server)
        NavigateDriverToPage("http://localhost:8888/Project.html");

        // Cas Login steps
        WebElement casUserName = driver.findElement(By.id("username"));
        WebElement casPwdName = driver.findElement(By.id("password"));

        casUserName.sendKeys("babm2002");
        casPwdName.sendKeys("password");

        casUserName.submit();
    }

    @Test
    public void testSearchUs() {

        OpenSearchUs();

        WebElement input = driver.findElement(By.id("gwt-debug-searchTextBox"));
        WebElement button = driver.findElement(By.id("gwt-debug-sendSearchButton"));

        Assert.assertNotNull("There should be an input in home page",
                input);
        Assert.assertNotNull("There should be a button in home page",
                button);

        input.sendKeys("Patate", Keys.ENTER);
        button.click();
    }
}
