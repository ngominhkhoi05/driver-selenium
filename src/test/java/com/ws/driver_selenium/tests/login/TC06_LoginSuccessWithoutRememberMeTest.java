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
 * TC06 – Happy path: username/password hợp lệ + KHÔNG tích Remember Me → rời trang login.
 * <p>
 * Disabled vì cần credential thật (giống TC05).
 */
@Epic("Đăng nhập UTC")
@Feature("Chức năng Đăng nhập")
@Story("Luồng đăng nhập & phiên")
@Severity(SeverityLevel.BLOCKER)
@Disabled("TC06 cần credential thật trên server UTC. Plan đang dùng placeholder " +
        "'huongnt/123456@utc' (xem TC05). BỎ @Disabled KHI có tài khoản thật.")
class TC06_LoginSuccessWithoutRememberMeTest extends BaseTest {

    @Test
    @DisplayName("TC06: Đăng nhập thành công khi KHÔNG tích Remember Me")
    void shouldLoginSuccessfullyWithoutRememberMe() {
        TestCaseSpec tc = HardcodedTestCases.TC6_LOGIN_SUCCESS_WITHOUT_REMEMBER;
        LoginData input = tc.getInput();

        Allure.addAttachment("Dữ liệu " + tc.getId(), "application/json", tc.toJson());

        LoginPage page = new LoginPage(driver)
                .open()
                .enterUsername(input.username())
                .enterPassword(input.password())
                .setRememberMe(input.rememberMe())  // false → no-op
                .clickLogin();

        page.assertLoggedInSuccessfully();
        assertThat(page.isOnLoginPage())
                .as("Đã rời trang login")
                .isFalse();
    }
}
