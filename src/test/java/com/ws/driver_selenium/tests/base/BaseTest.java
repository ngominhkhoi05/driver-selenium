package com.ws.driver_selenium.tests.base;

import com.ws.driver_selenium.core.AllureAttachment;
import com.ws.driver_selenium.core.DriverFactory;
import com.ws.driver_selenium.core.DriverManager;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class cho toàn bộ Selenium test trong project.
 * <p>
 * Mỗi test kế thừa class này sẽ tự động có:
 * <ul>
 *   <li>Một {@link WebDriver} riêng cho mỗi test (qua {@link DriverManager}).</li>
 *   <li>Screenshot + URL đính kèm vào Allure, dù test pass hay fail.</li>
 *   <li>Driver được {@code quit()} gọn gàng ở cuối test.</li>
 * </ul>
 * <p>
 * Lưu ý: cố ý KHÔNG dùng {@code try/finally} trong test, mà để JUnit lifecycle đảm bảo
 * {@code @AfterEach} luôn chạy (kể cả khi assertion fail).
 */
public abstract class BaseTest {

    protected static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    protected WebDriver driver;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        log.info("=== START: {} ===", testInfo.getDisplayName());
        Allure.parameter("testName", testInfo.getDisplayName());

        driver = DriverFactory.createChromeDriver();
        DriverManager.set(driver);
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        try {
            // Luôn chụp screenshot + URL trước khi quit, kể cả khi test pass/fail.
            AllureAttachment.attachScreenshot(driver, testInfo.getDisplayName() + " (screenshot)");
            AllureAttachment.attachCurrentUrl(driver, testInfo.getDisplayName() + " (url)");
        } finally {
            DriverManager.quit();
            driver = null;
            log.info("=== END: {} ===", testInfo.getDisplayName());
        }
    }
}
