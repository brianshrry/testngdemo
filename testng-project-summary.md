# TestNG Project Detailed Implementation Guide

## 1. WebDriver Management

Replace the current ChromeDriver initialization with WebDriverManager. This will resolve the access issues and make your tests more portable.

1. Add the WebDriverManager dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.bonigarcia</groupId>
    <artifactId>webdrivermanager</artifactId>
    <version>5.3.2</version>
    <scope>test</scope>
</dependency>
```

2. Update the `setup()` method in `WebAppTest.java`:

```java
import io.github.bonigarcia.wdm.WebDriverManager;

@BeforeClass
public void setup() {
    WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
    driver.manage().window().maximize();
    driver.get("https://example.com");
}
```

## 2. Page Object Model Implementation

Create a new package `com.automation.pages` and add page classes for each main page of your application. Here's an example for a login page:

```java
package com.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    private WebDriver driver;

    @FindBy(id = "username")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(id = "loginButton")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void login(String username, String password) {
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        loginButton.click();
    }
}
```

Update your `WebAppTest.java` to use the Page Object:

```java
import com.automation.pages.LoginPage;

public class WebAppTest {
    private WebDriver driver;
    private LoginPage loginPage;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://example.com");
        loginPage = new LoginPage(driver);
    }

    @Test(priority = 2)
    public void loginUser() {
        loginPage.login("testuser", "password123");
        // Add assertions here
    }
}
```

## 3. Configuration Management

Create a `config.properties` file in `src/test/resources`:

```properties
base.url=https://example.com
username=testuser
password=password123
```

Create a `ConfigManager` class to read from this file:

```java
package com.automation.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigManager {
    private static Properties props = new Properties();

    static {
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            props.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }
}
```

Update your tests to use this configuration:

```java
@BeforeClass
public void setup() {
    WebDriverManager.chromedriver().setup();
    driver = new ChromeDriver();
    driver.manage().window().maximize();
    driver.get(ConfigManager.getProperty("base.url"));
    loginPage = new LoginPage(driver);
}

@Test(priority = 2)
public void loginUser() {
    String username = ConfigManager.getProperty("username");
    String password = ConfigManager.getProperty("password");
    loginPage.login(username, password);
    // Add assertions here
}
```

## 4. Implement Wait Strategies

Replace `Thread.sleep()` calls with explicit waits:

```java
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "loginButton")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void login(String username, String password) {
        // ... (previous code)
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }
}
```

## 5. Improve Assertions

Add more detailed assertions to your tests:

```java
@Test(priority = 2)
public void loginUser() {
    loginPage.login(ConfigManager.getProperty("username"), ConfigManager.getProperty("password"));
    WebElement welcomeMessage = driver.findElement(By.id("welcome"));
    Assert.assertTrue(welcomeMessage.isDisplayed(), "Welcome message is not displayed after login");
    Assert.assertEquals(welcomeMessage.getText(), "Welcome, testuser!", "Incorrect welcome message after login");
}
```

## 6. Parallel Execution

Update your `testng.xml` file to enable parallel execution:

```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">
<suite name="Suite" parallel="methods" thread-count="3">
    <test name="Test">
        <classes>
            <class name="com.automation.tests.WebAppTest"/>
        </classes>
    </test>
</suite>
```

This configuration will run up to 3 test methods in parallel.

By implementing these changes, you'll significantly improve the robustness, maintainability, and efficiency of your test suite. Remember to update your `TestNGListener` and other parts of your code to align with these new patterns and practices.
