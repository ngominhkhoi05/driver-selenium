package com.ws.driver_selenium.tests.login;

import com.ws.driver_selenium.constants.LoginErrorMessages;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TC11 – Password phân biệt hoa/thường.
 * <p>
 * Username "huongnt" + password "123456@UTC" (đảo case từ "123456@utc")
 * → server UTC trả {@link LoginErrorMessages#INVALID_CREDENTIALS}.
 * <p>
 * Đã verify bằng Chrome thật (2026-10-08): server coi đảo case là sai.
 */
@Epic("Đăng nhập UTC")
@Feature("Chức năng Đăng nhập")
@Story("Password case-sensitive")
@Severity(SeverityLevel.NORMAL)
class TC11_PasswordCaseSensitiveTest extends BaseTest {

    @Test
    @DisplayName("TC11: Password đảo case → báo INVALID_CREDENTIALS")
    void shouldRejectPasswordWithSwappedCase() {
        TestCaseSpec tc = HardcodedTestCases.TC11_PASSWORD_CASE_SENSITIVE;
        LoginData input = tc.getInput();

        Allure.addAttachment("Dữ liệu " + tc.getId(), "application/json", tc.toJson());

        LoginPage page = new LoginPage(driver)
                .open()
                .enterUsername(input.username())
                .enterPassword(input.password())
                .clickLogin();

        assertThat(page.getErrorText())
                .as("Server UTC phân biệt hoa/thường")
                .isEqualTo(LoginErrorMessages.INVALID_CREDENTIALS);
        assertThat(page.isOnLoginPage())
                .as("Vẫn ở trang login")
                .isTrue();
    }
}
