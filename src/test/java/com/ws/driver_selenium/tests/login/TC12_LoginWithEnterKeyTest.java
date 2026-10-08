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
 * TC12 – Login bằng phím Enter (không cần click button).
 * <p>
 * Disabled vì cần credential thật (giống TC05, TC06).
 */
@Epic("Đăng nhập UTC")
@Feature("Chức năng Đăng nhập")
@Story("Phương thức submit form")
@Severity(SeverityLevel.NORMAL)
@Disabled("TC12 cần credential thật trên server UTC. Plan đang dùng placeholder " +
        "'huongnt/123456@utc' (xem TC05). BỎ @Disabled KHI có tài khoản thật.")
class TC12_LoginWithEnterKeyTest extends BaseTest {

    @Test
    @DisplayName("TC12: Đăng nhập thành công bằng phím Enter")
    void shouldLoginWithEnterKey() {
        TestCaseSpec tc = HardcodedTestCases.TC12_LOGIN_WITH_ENTER_KEY;
        LoginData input = tc.getInput();

        Allure.addAttachment("Dữ liệu " + tc.getId(), "application/json", tc.toJson());

        LoginPage page = new LoginPage(driver)
                .open()
                .enterUsername(input.username())
                .enterPassword(input.password())
                .submitWithEnter();

        page.assertLoggedInSuccessfully();
        assertThat(page.isOnLoginPage())
                .as("Đã rời trang login bằng phím Enter")
                .isFalse();
    }
}
