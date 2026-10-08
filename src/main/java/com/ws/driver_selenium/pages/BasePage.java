package com.ws.driver_selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Base class cho tất cả Page Object. Cung cấp các wrapper tiện ích:
 * <ul>
 *   <li>{@link #waitForVisible(By)}: đợi element xuất hiện trong DOM (visible).</li>
 *   <li>{@link #waitForClickable(By)}: đợi element có thể click.</li>
 *   <li>{@link #click(By)}, {@link #type(By, String)}, {@link #isVisible(By)}.</li>
 *   <li>{@link #getDriver()}: truy cập {@link WebDriver} của thread hiện tại.</li>
 * </ul>
 * <p>
 * Lưu ý: class này không thực hiện assertion — chỉ wrap hành vi Selenium
 * để các Page Object con (ví dụ {@link LoginPage}) có API gọn, dễ đọc hơn.
 */
public abstract class BasePage {

    protected static final Logger log = LoggerFactory.getLogger(BasePage.class);

    /** Timeout mặc định cho mọi wait trên page. */
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);

    private final WebDriver driver;

    protected BasePage(WebDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("WebDriver must not be null");
        }
        this.driver = driver;
    }

    /**
     * Cho phép Page Object con dùng driver trực tiếp khi cần (ví dụ navigate).
     * Ở đây ưu tiên lấy từ {@link com.ws.driver_selenium.core.DriverManager}
     * để đồng nhất với {@code BaseTest} (khi test chưa truyền driver vào).
     */
    protected WebDriver getDriver() {
        return driver;
    }

    public WebDriverWait wait(Duration timeout) {
        return new WebDriverWait(driver, timeout);
    }

    /** Tạo WebDriverWait với timeout mặc định (10s). */
    public WebDriverWait newWait() {
        return new WebDriverWait(driver, DEFAULT_TIMEOUT);
    }    public WebElement waitForVisible(By locator) {
        return newWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClickable(By locator) {
        return newWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public boolean isVisible(By locator) {
        try {
            return wait(Duration.ofSeconds(2))
                    .until(ExpectedConditions.visibilityOfElementLocated(locator)) != null;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void click(By locator) {
        waitForClickable(locator).click();
        log.debug("Clicked on {}", locator);
    }

    public void type(By locator, String text) {
        WebElement element = waitForVisible(locator);
        element.clear();
        if (text != null && !text.isEmpty()) {
            element.sendKeys(text);
        }
        log.debug("Typed '{}' into {}", text, locator);
    }

    public String getText(By locator) {
        return waitForVisible(locator).getText();
    }
}
