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
 * TC03 – Username đúng, password sai → báo {@link LoginErrorMessages#INVALID_CREDENTIALS}.
 */
@Epic("Đăng nhập UTC")
@Feature("Chức năng Đăng nhập")
@Story("Xác thực không hợp lệ")
@Severity(SeverityLevel.CRITICAL)
class TC03_WrongPasswordTest extends BaseTest {

    @Test
    @DisplayName("TC03: Sai mật khẩu với username hợp lệ")
    void shouldShowInvalidCredentialsWhenPasswordWrong() {
        TestCaseSpec tc = HardcodedTestCases.TC3_WRONG_PASSWORD;
        LoginData input = tc.getInput();

        Allure.addAttachment("Dữ liệu " + tc.getId(), "application/json", tc.toJson());

        LoginPage page = new LoginPage(driver)
                .open()
                .enterUsername(input.username())
                .enterPassword(input.password())
                .clickLogin();

        assertThat(page.getErrorText())
                .as("Lỗi khi password sai")
                .isEqualTo(LoginErrorMessages.INVALID_CREDENTIALS);
        assertThat(page.isOnLoginPage())
                .as("Vẫn ở trang login")
                .isTrue();
    }
}
