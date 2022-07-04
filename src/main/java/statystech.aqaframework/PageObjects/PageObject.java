package statystech.aqaframework.PageObjects;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.Loader;
import statystech.aqaframework.elements.Element;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public abstract class PageObject {

    private static final Logger logger = LoggerFactory.getLogger(MainPage.class);

    final int waitForElementDelay = 5;

    protected WebDriver webDriver;

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
