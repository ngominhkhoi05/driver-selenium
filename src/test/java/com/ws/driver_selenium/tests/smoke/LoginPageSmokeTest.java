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
    @DisplayName("Smoke: Remember Me checkbox đã bị UTC gỡ khỏi UI → isRememberMeSelected() luôn false")
    void rememberMeShouldHaveInitialState() {
        LoginPage loginPage = new LoginPage(DriverManager.get());
        loginPage.open();

        // UTC đã gỡ Remember Me khỏi form login (xác nhận bằng snapshot ngày 2026-10-08).
        // isRememberMeSelected() giờ là no-op, luôn trả về false và KHÔNG throw.
        boolean initial = loginPage.isRememberMeSelected();
        assertThat(initial)
                .as("Remember Me checkbox không còn trên UI → luôn false")
                .isFalse();
    }
}
