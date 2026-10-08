package com.ws.driver_selenium.tests.smoke;

import com.ws.driver_selenium.tests.base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test rỗng chỉ để verify Definition of Done của Phase 2:
 * <ul>
 *   <li>{@code BaseTest} tạo / đóng driver đúng.</li>
 *   <li>Driver navigate được tới một URL bất kỳ.</li>
 *   <li>Screenshot & URL đính kèm Allure.</li>
 * </ul>
 */
class BaseTestSmokeTest extends BaseTest {

    @Test
    @DisplayName("Smoke: BaseTest tạo driver và navigate thành công")
    void driverShouldBeUsable() {
        driver.get("https://example.com");

        assertThat(driver.getTitle())
                .as("Title của example.com")
                .contains("Example");
    }
}
