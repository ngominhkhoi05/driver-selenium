package com.ws.driver_selenium.tests.base;

import com.ws.driver_selenium.core.DriverFactory;
import com.ws.driver_selenium.core.DriverManager;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class cho toàn bộ Selenium test trong project.
 * <p>
 * Mỗi test kế thừa class này sẽ tự động có:
 * <ul>
 *   <li>Một {@link org.openqa.selenium.WebDriver} riêng cho mỗi test (qua {@link DriverManager}).</li>
 *   <li>Driver được {@code quit()} gọn gàng ở cuối test (trong {@code AllureScreenshotExtension},
 *       chạy SAU {@code @AfterEach} này).</li>
 * </ul>
 * <p>
 * <strong>Quan trọng</strong>: {@code BaseTest} KHÔNG quit driver trong {@code @AfterEach}
 * để {@link com.ws.driver_selenium.tests.hooks.AllureScreenshotExtension}
 * (JUnit extension) có thể chụp screenshot cuối cùng với status PASSED/FAILED đã biết.
 * Extension tự chịu trách nhiệm quit driver sau khi chụp xong.
 * <p>
 * Lưu ý: cố ý KHÔNG dùng {@code try/finally} trong test, mà để JUnit lifecycle đảm bảo
 * {@code @AfterEach} luôn chạy (kể cả khi assertion fail).
 */
public abstract class BaseTest {

    protected static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    protected org.openqa.selenium.WebDriver driver;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        log.info("=== START: {} ===", testInfo.getDisplayName());
        Allure.parameter("testName", testInfo.getDisplayName());

        driver = DriverFactory.createChromeDriver();
        DriverManager.set(driver);
    }
}
