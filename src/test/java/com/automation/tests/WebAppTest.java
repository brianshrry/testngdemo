package com.automation.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(com.automation.tests.TestNGListener.class)
public class WebAppTest {

    WebDriver driver;

    @BeforeClass
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Owner\\ZAP\\webdriver\\windows\\64");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://example.com");
    }

    @Test(priority = 1)
    public void registerUser() {
        driver.findElement(By.id("register")).click();
        driver.findElement(By.id("username")).sendKeys("testuser");
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("confirmPassword")).sendKeys("password123");
        driver.findElement(By.id("registerButton")).click();
        // Add assertions and additional steps as necessary
    }

    @Test(priority = 2)
    public void loginUser() {
        driver.findElement(By.id("login")).click();
        driver.findElement(By.id("username")).sendKeys("testuser");
        driver.findElement(By.id("password")).sendKeys("password123");
        driver.findElement(By.id("loginButton")).click();
        // Add assertions and additional steps as necessary
    }

    @Test(priority = 3)
    public void searchItem() {
        driver.findElement(By.id("searchBar")).sendKeys("item name");
        driver.findElement(By.id("searchButton")).click();
        // Add assertions and additional steps as necessary
    }

    @Test(priority = 4)
    public void addToCart() {
        driver.findElement(By.id("addToCartButton")).click();
        // Add assertions and additional steps as necessary
    }

    @Test(priority = 5)
    public void invalidLogin() {
        driver.findElement(By.id("login")).click();
        driver.findElement(By.id("username")).sendKeys("invaliduser");
        driver.findElement(By.id("password")).sendKeys("wrongpassword");
        driver.findElement(By.id("loginButton")).click();
        WebElement errorMessage = driver.findElement(By.id("errorMessage"));
        Assert.assertEquals(errorMessage.getText(), "Invalid login credentials");
    }

    @AfterClass
    public void teardown() {
        driver.quit();
    }
}
