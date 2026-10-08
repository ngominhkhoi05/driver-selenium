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

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TC17 – Click "Đăng nhập bằng e-mail UTC" → trigger OAuth Google flow.
 * <p>
 * Phát hiện thực tế (2026-10-08): khi click, server redirect tới {@code accounts.google.com}
 * nhưng Google reject vì {@code redirect_uri_mismatch} — cấu hình OAuth của app UTC bị sai
 * (không phải lỗi của test). Test chỉ verify OAuth flow ĐÃ ĐƯỢC trigger (URL chứa
 * {@code accounts.google.com}), không kiểm tra kết quả cuối cùng.
 */
@Epic("Đăng nhập UTC")
@Feature("Chức năng Đăng nhập")
@Story("Liên kết phụ: OAuth Google")
@Severity(SeverityLevel.NORMAL)
class TC17_GoogleOAuthLinkTest extends BaseTest {

    @Test
    @DisplayName("TC17: Click 'Đăng nhập bằng e-mail UTC' → navigate tới accounts.google.com")
    void shouldTriggerGoogleOAuthFlow() {
        TestCaseSpec tc = HardcodedTestCases.TC17_GOOGLE_OAUTH;

        Allure.addAttachment("Dữ liệu " + tc.getId(), "application/json", tc.toJson());

        LoginPage page = new LoginPage(driver)
                .open()
                .clickGoogleEmailLogin()
                .waitForUrlContains("accounts.google.com", Duration.ofSeconds(10));

        assertThat(page.getCurrentUrl())
                .as("OAuth flow được trigger: URL chứa accounts.google.com")
                .contains("accounts.google.com");
    }
}
