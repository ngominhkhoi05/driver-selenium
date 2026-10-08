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
 * TC20 – Username chứa payload SQL injection → server phải reject.
 * <p>
 * Payload cổ điển: {@code ' OR '1'='1} — nếu server không sanitize/escape, sẽ bypass
 * authentication. Server UTC trả {@link LoginErrorMessages#INVALID_CREDENTIALS} → an toàn.
 * <p>
 * Đã verify bằng Chrome thật (2026-10-08).
 */
@Epic("Đăng nhập UTC")
@Feature("Bảo mật")
@Story("SQL injection")
@Severity(SeverityLevel.CRITICAL)
class TC20_SqlInjectionTest extends BaseTest {

    @Test
    @DisplayName("TC20: Username chứa payload SQL injection → báo INVALID_CREDENTIALS")
    void shouldRejectSqlInjectionPayload() {
        TestCaseSpec tc = HardcodedTestCases.TC20_SQL_INJECTION;
        LoginData input = tc.getInput();

        Allure.addAttachment("Dữ liệu " + tc.getId(), "application/json", tc.toJson());

        LoginPage page = new LoginPage(driver)
                .open()
                .enterUsername(input.username())  // ' OR '1'='1
                .enterPassword(input.password())
                .clickLogin();

        assertThat(page.getErrorText())
                .as("Server reject SQLi (không bị bypass)")
                .isEqualTo(LoginErrorMessages.INVALID_CREDENTIALS);
        assertThat(page.isOnLoginPage())
                .as("Vẫn ở trang login (chưa bị bypass)")
                .isTrue();
    }
}
