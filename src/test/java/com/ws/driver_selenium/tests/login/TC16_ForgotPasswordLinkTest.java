package com.ws.driver_selenium.tests.login;

import com.ws.driver_selenium.data.HardcodedTestCases;
import com.ws.driver_selenium.model.TestCaseSpec;
import com.ws.driver_selenium.pages.LoginPage;
import com.ws.driver_selenium.tests.base.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TC16 – Click "Quên mật khẩu" → navigate tới trang {@code /Login/GetPass}.
 * <p>
 * Đã verify bằng Chrome thật (2026-10-08): server navigate thành công.
 */
@Epic("Đăng nhập UTC")
@Feature("Chức năng Đăng nhập")
@Story("Liên kết phụ: Quên mật khẩu")
@Severity(SeverityLevel.NORMAL)
class TC16_ForgotPasswordLinkTest extends BaseTest {

    @Test
    @DisplayName("TC16: Click 'Quên mật khẩu' → navigate /Login/GetPass")
    void shouldNavigateToGetPassPage() {
        TestCaseSpec tc = HardcodedTestCases.TC16_FORGOT_PASSWORD;

        Allure.addAttachment("Dữ liệu " + tc.getId(), "application/json", tc.toJson());

        LoginPage page = new LoginPage(driver)
                .open()
                .clickForgotPassword()
                .waitForUrlContains("/Login/GetPass", Duration.ofSeconds(10));

        assertThat(page.getCurrentUrl())
                .as("URL chứa /Login/GetPass")
                .contains("/Login/GetPass");
    }
}
