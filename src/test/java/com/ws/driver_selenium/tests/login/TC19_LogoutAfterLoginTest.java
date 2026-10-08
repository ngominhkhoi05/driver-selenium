package com.ws.driver_selenium.tests.login;

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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TC19 – Login rồi logout → quay về trang /Login.
 * <p>
 * Disabled vì cần credential thật để login thành công trước.
 * <p>
 * Lưu ý: cần thêm method {@code clickLogout()} vào Page Object khi bật lại test
 * (sau khi có credential thật).
 */
@Epic("Đăng nhập UTC")
@Feature("Chức năng Đăng nhập")
@Story("Quản lý phiên")
@Severity(SeverityLevel.NORMAL)
@Disabled("TC19 cần credential thật để login trước, sau đó mới logout được. " +
        "BỎ @Disabled + bổ sung LoginPage.clickLogout() KHI có tài khoản thật.")
class TC19_LogoutAfterLoginTest extends BaseTest {

    @Test
    @DisplayName("TC19: Logout sau khi login → URL quay về /Login")
    void shouldRedirectToLoginAfterLogout() {
        TestCaseSpec tc = HardcodedTestCases.TC19_LOGOUT;
        LoginData input = tc.getInput();

        Allure.addAttachment("Dữ liệu " + tc.getId(), "application/json", tc.toJson());

        LoginPage page = new LoginPage(driver)
                .open()
                .enterUsername(input.username())
                .enterPassword(input.password())
                .setRememberMe(input.rememberMe())
                .clickLogin();

        page.assertLoggedInSuccessfully();

        // TODO: page.clickLogout();   // cần thêm method này vào LoginPage

        assertThat(page.isOnLoginPage())
                .as("Sau logout URL quay về /Login")
                .isTrue();
    }
}
