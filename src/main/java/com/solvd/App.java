package com.solvd;

import com.solvd.pages.HomePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class App {
    public static void main(String[] args) {
        ChromeOptions chromeOptions = new ChromeOptions();
        WebDriver driver = null;
        try {
            driver = new RemoteWebDriver(new URL("http://localhost:4444"), chromeOptions);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        driver.get("https://magento.softwaretestingboard.com/");
        HomePage homePage = new HomePage(driver);
        homePage.searchForProduct("Backpack");
        //driver.quit();
    }
}
