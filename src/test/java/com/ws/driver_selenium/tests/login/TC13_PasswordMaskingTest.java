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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TC13 – Ô password phải có {@code type="password"} để browser mask ký tự nhập vào.
 * <p>
 * Đây là test UI/DOM, không phụ thuộc server UTC.
 */
@Epic("Đăng nhập UTC")
@Feature("Chức năng Đăng nhập")
@Story("Bảo mật giao diện")
@Severity(SeverityLevel.MINOR)
class TC13_PasswordMaskingTest extends BaseTest {

    @Test
    @DisplayName("TC13: Ô password có type='password' (mask ký tự)")
    void shouldHavePasswordFieldTypeAsPassword() {
        TestCaseSpec tc = HardcodedTestCases.TC13_PASSWORD_MASKING;

        Allure.addAttachment("Dữ liệu " + tc.getId(), "application/json", tc.toJson());

        LoginPage page = new LoginPage(driver).open();

        String type = page.getPasswordFieldType();
        assertThat(type)
                .as("Password field phải có type='password' để browser tự mask")
                .isEqualTo("password");
    }
}
