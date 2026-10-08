package com.ws.driver_selenium.tests.smoke;

import com.ws.driver_selenium.core.DriverManager;
import com.ws.driver_selenium.pages.LoginPage;
import com.ws.driver_selenium.tests.base.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Smoke test cho Phase 3: chỉ verify Page Object {@link LoginPage}
 * có thể gọi {@code open()} mà không lỗi selector, và đọc được các
 * element cơ bản (form, error div) trên trang thật.
 * <p>
 * Test này chạy thật trên trang login UTC — KHÔNG nhập username/password,
 * nên phù hợp làm DoD của "Page Object" trước khi có data test ở Phase 4.
 */
class LoginPageSmokeTest extends BaseTest {

    @Test
    @DisplayName("Smoke: LoginPage.open() load được form login")
    void loginPageShouldOpen() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.open();

        assertThat(loginPage.isOnLoginPage())
                .as("Đang ở trang login")
                .isTrue();
    }

    @Test
    @DisplayName("Smoke: LoginPage có thể đọc trạng thái Remember Me ban đầu")
    void rememberMeShouldHaveInitialState() {
        LoginPage loginPage = new LoginPage(DriverManager.get());
        loginPage.open();

        // Không assert giá trị cụ thể vì phụ thuộc vào trang thật,
        // chỉ verify rằng isRememberMeSelected() không ném exception.
        boolean initial = loginPage.isRememberMeSelected();
        assertThat(initial).isIn(true, false);
    }
}
