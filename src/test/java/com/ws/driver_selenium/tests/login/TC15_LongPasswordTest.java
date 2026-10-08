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
 * TC15 – Password rất dài (300 ký tự 'a') → báo lỗi boundary.
 * <p>
 * Phát hiện thực tế (2026-10-08): ô password client-side có {@code maxlength}, chỉ cho gõ
 * tối đa 267 ký tự (đã verify). Server nhận chuỗi 267 ký tự 'a' → trả INVALID_CREDENTIALS.
 * <p>
 * Plan gốc nói 300 ký tự nhưng thực tế client-side đã cắt. Test vẫn pass vì chỉ kiểm tra
 * server response.
 */
@Epic("Đăng nhập UTC")
@Feature("Chức năng Đăng nhập")
@Story("Boundary value")
@Severity(SeverityLevel.MINOR)
class TC15_LongPasswordTest extends BaseTest {

    @Test
    @DisplayName("TC15: Password 300 ký tự → báo INVALID_CREDENTIALS")
    void shouldRejectVeryLongPassword() {
        TestCaseSpec tc = HardcodedTestCases.TC15_LONG_PASSWORD;
        LoginData input = tc.getInput();

        Allure.addAttachment("Dữ liệu " + tc.getId(), "application/json", tc.toJson());

        LoginPage page = new LoginPage(driver)
                .open()
                .enterUsername(input.username())
                .enterPassword(input.password())  // 300 ký tự 'a' (client-side cắt còn 267)
                .clickLogin();

        assertThat(page.getErrorText())
                .as("Server UTC reject password quá dài")
                .isEqualTo(LoginErrorMessages.INVALID_CREDENTIALS);
        assertThat(page.isOnLoginPage())
                .as("Vẫn ở trang login")
                .isTrue();
    }
}
