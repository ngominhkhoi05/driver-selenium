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
 * TC09 – Username chỉ chứa khoảng trắng → server UTC trả {@link LoginErrorMessages#INVALID_CREDENTIALS}.
 * <p>
 * Phát hiện thực tế (2026-10-08): server KHÔNG trim() whitespace trước khi validate.
 * Username "   " được coi là credential không hợp lệ, không phải empty.
 * Plan gốc expected EMPTY_USERNAME → đã sửa data thành INVALID_CREDENTIALS.
 */
@Epic("Đăng nhập UTC")
@Feature("Chức năng Đăng nhập")
@Story("Validation đầu vào")
@Severity(SeverityLevel.NORMAL)
class TC09_UsernameOnlyWhitespaceTest extends BaseTest {

    @Test
    @DisplayName("TC09: Username chỉ chứa khoảng trắng")
    void shouldShowEmptyUsernameWhenUsernameOnlyWhitespace() {
        TestCaseSpec tc = HardcodedTestCases.TC9_USERNAME_ONLY_WHITESPACE;
        LoginData input = tc.getInput();

        Allure.addAttachment("Dữ liệu " + tc.getId(), "application/json", tc.toJson());

        LoginPage page = new LoginPage(driver)
                .open()
                .enterUsername(input.username())  // "   "
                .enterPassword(input.password())
                .clickLogin();

        assertThat(page.getErrorText())
                .as("Server UTC coi '   ' là credential không hợp lệ (không trim)")
                .isEqualTo(LoginErrorMessages.INVALID_CREDENTIALS);
        assertThat(page.isOnLoginPage())
                .as("Vẫn ở trang login")
                .isTrue();
    }
}
