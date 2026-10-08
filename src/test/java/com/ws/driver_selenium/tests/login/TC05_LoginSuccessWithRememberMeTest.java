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
 * TC05 – Happy path: username/password hợp lệ + tích Remember Me → rời trang login.
 * <p>
 * Yêu cầu môi trường: tài khoản {@code huongnt / 123456@utc} tồn tại trên hệ thống UTC.
 * Nếu test này fail vì credential thật không khớp, vẫn xem như infrastructure OK —
 * vấn đề nằm ở data, không phải ở Page Object / BaseTest.
 */
@Epic("Đăng nhập UTC")
@Feature("Chức năng Đăng nhập")
@Story("Luồng đăng nhập & phiên")
@Severity(SeverityLevel.BLOCKER)
@Disabled("TC05 cần credential thật trên server UTC. Plan đang dùng placeholder " +
        "'huongnt/123456@utc' từ bản Python cũ, server UTC trả về 'Mật khẩu không đúng' " +
        "(đã verify bằng Chrome thật ngày 2026-10-08). " +
        "BỎ @Disabled KHI có tài khoản thật: thay credential trong " +
        "HardcodedTestCases.TC5_LOGIN_SUCCESS_WITH_REMEMBER rồi xoá annotation này.")
class TC05_LoginSuccessWithRememberMeTest extends BaseTest {

    @Test
    @DisplayName("TC05: Đăng nhập thành công khi tích Remember Me")
    void shouldLoginSuccessfullyWithRememberMe() {
        TestCaseSpec tc = HardcodedTestCases.TC5_LOGIN_SUCCESS_WITH_REMEMBER;
        LoginData input = tc.getInput();

        Allure.addAttachment("Dữ liệu " + tc.getId(), "application/json", tc.toJson());

        LoginPage page = new LoginPage(driver)
                .open()
                .enterUsername(input.username())
                .enterPassword(input.password())
                .setRememberMe(input.rememberMe())
                .clickLogin();

        // Sau khi login thành công: URL phải rời khỏi /Login.
        page.assertLoggedInSuccessfully();
        assertThat(page.isOnLoginPage())
                .as("Đã rời trang login")
                .isFalse();
    }
}
