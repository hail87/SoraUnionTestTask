package statystech.aqaframework.PageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.Loader;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.function.Predicate;

public abstract class PageObject {

    private static final Logger logger = LoggerFactory.getLogger(MainPage.class);

    final int waitForElementDelay = 5;
    final int waitForJSDelay = 30;

    protected WebDriver webDriver;

//    protected void waitForPageToLoad() {
//        WebDriverWait wait = new WebDriverWait(webDriver, waitForElementDelay);
//        wait.until( new Predicate<WebDriver>() {
//                        public boolean apply(WebDriver driver) {
//                            return ((JavascriptExecutor)driver).executeScript("return document.readyState").equals("complete");
//                        }
//                    }
//        );
//    }

    public boolean waitForJStoLoad() {

        WebDriverWait wait = new WebDriverWait(webDriver, waitForJSDelay);
        JavascriptExecutor js = (JavascriptExecutor)webDriver;

        // wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    return ((Long)js.executeScript("return jQuery.active") == 0);
                }
                catch (Exception e) {
                    return true;
                }
            }
        };

        // wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return js.executeScript("return document.readyState")
                        .toString().equals("complete");
            }
        };

        return wait.until(jQueryLoad) && wait.until(jsLoad);
    }

    public boolean waitForElementToUpdate(WebDriver webDriver, By locator) {
        WebElement element = webDriver.findElement(locator);
        String baseValue = element.getText();
        logger.info(("Element with locator '" + locator + "' base value - " + baseValue));
        int i = 0;
        while(baseValue.equals(element.getText()) & i<=10){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }
        return !baseValue.equals(element.getText());

//        WebDriverWait wait = new WebDriverWait(webDriver, 10);
//
//        ExpectedCondition<Boolean> isElementChanged = new ExpectedCondition<Boolean>() {
//            @Override
//            public Boolean apply(WebDriver driver) {
//                try {
//                    return baseValue.equals(element.getText());
//                }
//                catch (Exception e) {
//                    return true;
//                }
//            }
//        };
//
//        return wait.until(isElementChanged);
    }

    protected void waitForElementToLoad(By by) {
        WebDriverWait wait = new WebDriverWait(webDriver, waitForElementDelay);
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    protected void waitForElementToBeClickable(By by) {
        WebDriverWait wait = new WebDriverWait(webDriver, waitForElementDelay);
        wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    protected void waitForElementToDisappear(WebElement element) {
        WebDriverWait wait = new WebDriverWait(webDriver, waitForElementDelay);
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    protected void waitForElementToDisappear(By xpath) {
        WebDriverWait wait = new WebDriverWait(webDriver, waitForElementDelay);
        wait.until(ExpectedConditions.invisibilityOf(webDriver.findElement(xpath)));
    }

    public String verifyElementIsDisappear(By xpath) {
        try {
            WebElement webElement = webDriver.findElement(xpath);
            if (webElement.isDisplayed()) {
                return "onHoldDate element is still visible";
            } else {
                return "";
            }
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    protected void waitForButtonToBeClickable(By by) {
        WebDriverWait wait = new WebDriverWait(webDriver, waitForElementDelay);
        wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    protected void scrollToElement(WebElement webElement) throws Exception {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoViewIfNeeded()", webElement);
        Thread.sleep(500);
    }

    protected WebElement findElementByText(String text) {
        logger.info("Looking for element with text : " + text);
        return webDriver.findElement(By.xpath("//div[text()=\"" + text + "\"]"));
    }

    protected void unfocus() {
        webDriver.findElement(By.xpath("//body")).click();
    }

    public WebDriver switchTab(int tabNumber) {
        if (tabNumber == 0)
            throw new IllegalArgumentException("tabNumber should be > 0");
        ArrayList<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
        int i = 0;
        while (tabs.size() <= 1 & i < 10) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            tabs = new ArrayList<>(webDriver.getWindowHandles());
            i++;
        }
        webDriver.switchTo().window(tabs.get(tabNumber - 1));
        return webDriver;
    }

    public String waitForTabsSize(int tabSize) {
        ArrayList<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
        if (tabs.size() != tabSize) {
            int i = 0;
            while (tabs.size() <= 1 & i < 10) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tabs = new ArrayList<>(webDriver.getWindowHandles());
                if (tabs.size() == tabSize)
                    return "";
                i++;
            }
            return "\nExpected tab size '" + tabSize + "', but actual is '" + tabs.size() + "'";
        } else {
            return "";
        }
    }

    public String readPDFContent(String appUrl) throws Exception {

        URL url = new URL(appUrl);
        InputStream input = url.openStream();
        BufferedInputStream fileToParse = new BufferedInputStream(input);
        PDDocument document = null;
        String output = null;

        try {
            document = Loader.loadPDF(fileToParse);
            output = new PDFTextStripper().getText(document);
            System.out.println(output);

        } finally {
            if (document != null) {
                document.close();
            }
            fileToParse.close();
            input.close();
        }
        return output;
    }

}
