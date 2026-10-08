package com.ws.driver_selenium.core;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lưu trữ {@link WebDriver} theo từng thread (ThreadLocal) để chuẩn bị cho
 * việc chạy test song song sau này (JUnit 5 {@code @Execution(CONCURRENT)}).
 * <p>
 * Quy ước:
 * <ul>
 *   <li>{@link #set(WebDriver)}: gọi 1 lần trong {@code @BeforeEach} của {@code BaseTest}.</li>
 *   <li>{@link #get()}: lấy driver hiện tại trong các step / page object.</li>
 *   <li>{@link #quit()}: gọi trong {@code @AfterEach} (kể cả khi test lỗi).</li>
 * </ul>
 */
public final class DriverManager {

    private static final Logger log = LoggerFactory.getLogger(DriverManager.class);

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void set(WebDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("WebDriver must not be null");
        }
        DRIVER.set(driver);
        log.debug("Driver bound to thread {}", Thread.currentThread().getId());
    }

    public static WebDriver get() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException(
                    "WebDriver is not initialized for thread "
                            + Thread.currentThread().getId()
                            + ". Did you forget to call DriverManager.set(...) in @BeforeEach?");
        }
        return driver;
    }

    /**
     * Lấy driver hiện tại của thread, hoặc {@code null} nếu chưa {@link #set(WebDriver)}.
     * <p>
     * Dùng cho các extension/test không yêu cầu driver (ví dụ {@code HardcodedTestCasesTest}).
     * Khác với {@link #get()}: method này KHÔNG throw exception khi driver null.
     */
    public static WebDriver getOrNull() {
        return DRIVER.get();
    }

    public static void quit() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
                log.debug("Driver quit for thread {}", Thread.currentThread().getId());
            } catch (Exception e) {
                log.warn("Error while quitting driver: {}", e.getMessage());
            } finally {
                DRIVER.remove();
            }
        }
    }
}
