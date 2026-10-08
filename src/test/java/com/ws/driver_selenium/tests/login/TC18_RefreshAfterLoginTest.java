package com.ws.driver_selenium.tests.login;

import com.ws.driver_selenium.data.HardcodedTestCases;
import com.ws.driver_selenium.model.LoginData;
import com.ws.driver_selenium.model.TestCaseSpec;
import com.ws.driver_selenium.pages.LoginPage;
import com.ws.driver_selenium.tests.base.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TC18 – Sau khi login thành công → refresh trang → vẫn ở trạng thái login (session persistent).
 * <p>
 * Disabled vì cần credential thật (giống TC05, TC06, TC12).
 */
@Epic("Đăng nhập UTC")
@Feature("Chức năng Đăng nhập")
@Story("Quản lý phiên")
@Severity(SeverityLevel.NORMAL)
@Disabled("TC18 cần credential thật trên server UTC. Plan đang dùng placeholder " +
        "'huongnt/123456@utc' (xem TC05). BỎ @Disabled KHI có tài khoản thật.")
class TC18_RefreshAfterLoginTest extends BaseTest {

    @Test
    @DisplayName("TC18: Refresh trang sau khi login → vẫn ở trạng thái login")
    void shouldStayLoggedInAfterRefresh() {
        TestCaseSpec tc = HardcodedTestCases.TC18_REFRESH_AFTER_LOGIN;
        LoginData input = tc.getInput();

        Allure.addAttachment("Dữ liệu " + tc.getId(), "application/json", tc.toJson());

        LoginPage page = new LoginPage(driver)
                .open()
                .enterUsername(input.username())
                .enterPassword(input.password())
                .setRememberMe(input.rememberMe())
                .clickLogin();

        page.assertLoggedInSuccessfully();

        // Refresh và verify vẫn ở ngoài trang login (session còn hiệu lực)
        driver.navigate().refresh();

        assertThat(page.isOnLoginPage())
                .as("Sau refresh vẫn ở ngoài trang login → session persistent")
                .isFalse();
    }
}
