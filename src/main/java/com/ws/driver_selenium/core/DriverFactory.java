package com.ws.driver_selenium.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Tạo {@link WebDriver} (hiện tại chỉ hỗ trợ Chrome).
 * <p>
 * Selenium 4 có sẵn <a href="https://www.selenium.dev/documentation/selenium_manager/">Selenium Manager</a>,
 * nên không cần tải chromedriver thủ công — thư viện tự động tìm version tương thích.
 */
public final class DriverFactory {

    private static final Logger log = LoggerFactory.getLogger(DriverFactory.class);

    /** Thời gian chờ ngầm định cho mọi lệnh findElement / navigate. */
    public static final Duration DEFAULT_IMPLICIT_WAIT = Duration.ofSeconds(10);

    /** Thời gian chờ tối đa cho page load hoàn tất. */
    public static final Duration DEFAULT_PAGE_LOAD_TIMEOUT = Duration.ofSeconds(30);

    private DriverFactory() {
    }

    /**
     * Tạo ChromeDriver mới với options từ {@link ChromeOptionsFactory}.
     * Driver phải được {@code quit()} khi test kết thúc (xem {@link DriverManager}).
     */
    public static WebDriver createChromeDriver() {
        log.info("Creating ChromeDriver (headless={})", ChromeOptionsFactory.isHeadless());

        ChromeDriver driver = new ChromeDriver(ChromeOptionsFactory.build());

        driver.manage().timeouts().implicitlyWait(DEFAULT_IMPLICIT_WAIT);
        driver.manage().timeouts().pageLoadTimeout(DEFAULT_PAGE_LOAD_TIMEOUT);
        driver.manage().window().maximize();

        return driver;
    }
}
