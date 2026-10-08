package com.ws.driver_selenium.tests.hooks;

import com.ws.driver_selenium.core.AllureAttachment;
import com.ws.driver_selenium.core.DriverManager;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * JUnit 5 Extension tự động đính kèm screenshot + URL vào Allure report
 * <strong>sau khi mỗi test kết thúc</strong> (cả PASSED lẫn FAILED), với tên
 * theo format plan: {@code Screenshot [PASSED|FAILED] - TCxx_<Ten>}.
 * <p>
 * Auto-registered qua SPI file
 * {@code src/test/resources/META-INF/services/org.junit.jupiter.api.extension.Extension}.
 * Áp dụng cho TOÀN BỘ test, không cần {@code @ExtendWith}.
 *
 * <h2>Cơ chế hoạt động (JUnit 5 Lifecycle)</h2>
 * Thứ tự callbacks cho mỗi test:
 * <ol>
 *   <li>{@code BeforeEachCallback} (extension) — không dùng.</li>
 *   <li>{@code @BeforeEach} của {@code BaseTest} — tạo driver.</li>
 *   <li>Test method chạy.</li>
 *   <li>{@code @AfterEach} của {@code BaseTest} — hiện không làm gì (driver chưa quit).</li>
 *   <li>{@code AfterEachCallback} (extension) — chụp screenshot + URL + quit driver.</li>
 *   <li>{@code TestWatcher.testSuccessful/Failed/Disabled/Aborted} (extension) — ghi status
 *       vào Allure.</li>
 * </ol>
 * <p>
 * Status (PASSED/FAILED) đọc từ {@link ExtensionContext#getExecutionException()}.
 *
 * <h2>Quy ước tên attachment</h2>
 * <ul>
 *   <li>Class test theo convention {@code TCxx_<Ten>Test} → {@code TCxx} lấy từ tên class.</li>
 *   <li>{@code <Ten>} lấy từ {@code displayName} của method.</li>
 * </ul>
 * Ví dụ: class {@code TC07_BothFieldsEmptyTest} + displayName {@code TC07: Cả username và password đều trống}
 * → tên attachment: {@code Screenshot PASSED - TC07_TC07: Cả username và password đều trống}.
 */
public class AllureScreenshotExtension implements AfterEachCallback, TestWatcher {

    private static final Logger log = LoggerFactory.getLogger(AllureScreenshotExtension.class);
    private static final String TC_PREFIX = "TC";
    private static final String SEPARATOR = " - ";

    @Override
    public void testSuccessful(ExtensionContext context) {
        // Không cần làm gì — afterEach() đã chụp screenshot + URL.
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        // Không cần làm gì — afterEach() đã chụp screenshot + URL (với status FAILED).
    }

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        // Test bị @Disabled → driver chưa được tạo, afterEach() skip.
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        // Không cần làm gì.
    }

    @Override
    public void afterEach(ExtensionContext context) {
        WebDriver driver = DriverManager.getOrNull();
        if (driver == null) {
            log.debug("Driver null → skip screenshot cho {}", context.getDisplayName());
            return;
        }

        try {
            // Detect status từ extension context.
            String status = "PASSED";
            Optional<Throwable> exception = context.getExecutionException();
            if (exception != null && exception.isPresent()) {
                status = "FAILED";
            }

            String tcId = extractTcId(context);
            String displayName = context.getDisplayName();
            String attachmentName = "Screenshot " + status + SEPARATOR + tcId + "_" + displayName;

            if (driver instanceof TakesScreenshot takesScreenshot) {
                try {
                    byte[] png = takesScreenshot.getScreenshotAs(OutputType.BYTES);
                    Allure.addAttachment(
                            attachmentName,
                            "image/png",
                            new java.io.ByteArrayInputStream(png),
                            ".png"
                    );
                    Allure.parameter("lastScreenshot", attachmentName);
                    log.debug("Đã attach '{}' ({} bytes)", attachmentName, png.length);
                } catch (Exception e) {
                    log.warn("Không chụp được screenshot cho {}: {}",
                            context.getDisplayName(), e.getMessage());
                }
            }

            AllureAttachment.attachCurrentUrl(driver, "URL after " + status + ": " + displayName);
        } finally {
            // Quit driver SAU khi chụp xong.
            try {
                DriverManager.quit();
            } catch (Exception e) {
                log.warn("Lỗi khi quit driver: {}", e.getMessage());
            }
        }
    }

    /**
     * Trích {@code TCxx} từ tên class test.
     * Ví dụ: {@code TC07_BothFieldsEmptyTest} → {@code TC07}.
     */
    private String extractTcId(ExtensionContext context) {
        String className = context.getRequiredTestClass().getSimpleName();
        int underscore = className.indexOf('_');
        if (underscore > 0 && className.startsWith(TC_PREFIX)) {
            return className.substring(0, underscore);
        }
        return className;
    }
}
