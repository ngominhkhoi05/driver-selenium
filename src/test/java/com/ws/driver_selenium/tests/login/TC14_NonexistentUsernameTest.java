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
 * TC14 – Username không tồn tại trong hệ thống → báo lỗi.
 * <p>
 * Phân biệt với TC03 (sai password): TC03 dùng username hợp lệ, TC14 dùng username không có thật.
 * Cả hai đều trả cùng {@link LoginErrorMessages#INVALID_CREDENTIALS} (server UTC không phân biệt
 * 2 loại lỗi này — chống user enumeration).
 * <p>
 * Đã verify bằng Chrome thật (2026-10-08).
 */
@Epic("Đăng nhập UTC")
@Feature("Chức năng Đăng nhập")
@Story("Xác thực không hợp lệ")
@Severity(SeverityLevel.NORMAL)
class TC14_NonexistentUsernameTest extends BaseTest {

    @Test
    @DisplayName("TC14: Username không tồn tại → báo INVALID_CREDENTIALS")
    void shouldShowInvalidCredentialsForNonexistentUsername() {
        TestCaseSpec tc = HardcodedTestCases.TC14_NONEXISTENT_USERNAME;
        LoginData input = tc.getInput();

        Allure.addAttachment("Dữ liệu " + tc.getId(), "application/json", tc.toJson());

        LoginPage page = new LoginPage(driver)
                .open()
                .enterUsername(input.username())
                .enterPassword(input.password())
                .clickLogin();

        assertThat(page.getErrorText())
                .as("Server UTC trả cùng lỗi cho cả 'user không tồn tại' và 'sai password' (chống enumeration)")
                .isEqualTo(LoginErrorMessages.INVALID_CREDENTIALS);
        assertThat(page.isOnLoginPage())
                .as("Vẫn ở trang login")
                .isTrue();
    }
}
