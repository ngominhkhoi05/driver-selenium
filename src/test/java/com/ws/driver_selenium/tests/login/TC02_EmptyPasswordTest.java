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
 * TC02 – Bỏ trống password → server trả lỗi {@link LoginErrorMessages#EMPTY_PASSWORD}.
 */
@Epic("Đăng nhập UTC")
@Feature("Chức năng Đăng nhập")
@Story("Validation đầu vào")
@Severity(SeverityLevel.CRITICAL)
class TC02_EmptyPasswordTest extends BaseTest {

    @Test
    @DisplayName("TC02: Bỏ trống password")
    void shouldShowEmptyPasswordError() {
        TestCaseSpec tc = HardcodedTestCases.TC2_EMPTY_PASSWORD;
        LoginData input = tc.getInput();

        Allure.addAttachment("Dữ liệu " + tc.getId(), "application/json", tc.toJson());

        LoginPage page = new LoginPage(driver)
                .open()
                .enterUsername(input.username())
                .enterPassword(input.password())
                .clickLogin();

        assertThat(page.getErrorText())
                .as("Lỗi khi để trống password")
                .isEqualTo(LoginErrorMessages.EMPTY_PASSWORD);
        assertThat(page.isOnLoginPage())
                .as("Vẫn ở trang login")
                .isTrue();
    }
}
