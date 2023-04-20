package statystech.aqaframework.PageObjects;

import lombok.Getter;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statystech.aqaframework.elements.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainPage extends PageObject {

    private static final Logger logger = LoggerFactory.getLogger(MainPage.class);
    private final WebDriver webDriver;
    By btnBookAppointment = By.xpath("//button[@type=\"submit\"]");

    @Getter
    String url;

    public MainPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        super.webDriver = webDriver;
        waitForJStoLoad();
        waitForElementToLoad(btnBookAppointment);
        url = webDriver.getCurrentUrl();
        verifyPageHeader();
    }

    private void verifyPageHeader() {
        Assert.assertTrue("\nText 'Make Appointment' is NOT shown, but should\n", isTextShownAtThePage("Make Appointment"));
    }

}
