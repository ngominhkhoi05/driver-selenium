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
 * TC07 – Cả username và password đều trống → server trả {@link LoginErrorMessages#EMPTY_USERNAME}.
 * <p>
 * Hành vi server UTC: validation ưu tiên trường username trước.
 */
@Epic("Đăng nhập UTC")
@Feature("Chức năng Đăng nhập")
@Story("Validation đầu vào")
@Severity(SeverityLevel.CRITICAL)
class TC07_BothFieldsEmptyTest extends BaseTest {

    @Test
    @DisplayName("TC07: Cả username và password đều trống")
    void shouldShowEmptyUsernameWhenBothEmpty() {
        TestCaseSpec tc = HardcodedTestCases.TC7_BOTH_FIELDS_EMPTY;
        LoginData input = tc.getInput();

        Allure.addAttachment("Dữ liệu " + tc.getId(), "application/json", tc.toJson());

        LoginPage page = new LoginPage(driver)
                .open()
                .enterUsername(input.username())
                .enterPassword(input.password())
                .clickLogin();

        assertThat(page.getErrorText())
                .as("Lỗi khi cả 2 field trống")
                .isEqualTo(LoginErrorMessages.EMPTY_USERNAME);
        assertThat(page.isOnLoginPage())
                .as("Vẫn ở trang login")
                .isTrue();
    }
}
