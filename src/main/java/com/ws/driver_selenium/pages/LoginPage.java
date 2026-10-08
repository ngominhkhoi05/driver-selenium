package com.ws.driver_selenium.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Page Object cho trang đăng nhập Văn phòng điện tử UTC.
 * <p>
 * Tuân thủ Page Object Model:
 * <ul>
 *   <li>Chỉ chứa locator + hành động (action), KHÔNG chứa assertion.</li>
 *   <li>Mỗi action gắn {@link Step} để hiện trong báo cáo Allure.</li>
 *   <li>Các assertion phải đặt ở test class.</li>
 * </ul>
 */
public class LoginPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(LoginPage.class);

    /** URL trang đăng nhập (kèm query string {@code r} để redirect về trang chủ). */
    public static final String LOGIN_URL =
            "https://vanphongdientu.utc.edu.vn/Login?r=https%3A%2F%2Fvanphongdientu.utc.edu.vn%2F";

    // ---------------- Locators ----------------
    // Lưu ý: Trang login UTC thật KHÔNG còn Remember Me checkbox nữa
    // (đã bị loại bỏ khỏi UI). Giữ method setRememberMe() / isRememberMeSelected()
    // ở dạng no-op an toàn để tương thích với HardcodedTestCases (vẫn có field rememberMe).
    private static final By FORM           = By.cssSelector("form[action='/Login'][method='post']");
    private static final By USERNAME_INPUT = By.cssSelector("input[name='username']");
    private static final By PASSWORD_INPUT = By.cssSelector("input[name='userpwd']");
    private static final By GOOGLE_OAUTH   = By.cssSelector("a.button");
    private static final By SUBMIT_BUTTON  = By.cssSelector("input.submit_login[type='submit']");
    private static final By FORGOT_LINK    = By.cssSelector("div.helps a[href='/Login/GetPass']");
    private static final By ERROR_DIV      = By.cssSelector("div.error");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // ---------------- Navigation ----------------

    @Step("Mở trang đăng nhập UTC")
    public LoginPage open() {
        getDriver().get(LOGIN_URL);
        // Đợi form xuất hiện để chắc chắn trang đã render xong.
        waitForVisible(FORM);
        log.info("Opened login page: {}", LOGIN_URL);
        return this;
    }

    // ---------------- Actions ----------------

    @Step("Nhập username: {0}")
    public LoginPage enterUsername(String username) {
        type(USERNAME_INPUT, username);
        return this;
    }

    @Step("Nhập password")
    public LoginPage enterPassword(String password) {
        type(PASSWORD_INPUT, password);
        return this;
    }

    @Step("Set Remember Me = {0} (no-op: checkbox không còn trên trang UTC)")
    public LoginPage setRememberMe(boolean checked) {
        // Trang login UTC hiện tại không có Remember Me checkbox nữa.
        // Method giữ lại để tương thích API với HardcodedTestCases;
        // chỉ log để tiện debug, không throw, không làm hỏng test.
        log.warn("setRememberMe({}) ignored: Remember Me checkbox không còn trên trang login UTC", checked);
        return this;
    }

    @Step("Click nút Login")
    public LoginPage clickLogin() {
        click(SUBMIT_BUTTON);
        return this;
    }

    @Step("Submit bằng phím Enter")
    public LoginPage submitWithEnter() {
        WebElement password = waitForVisible(PASSWORD_INPUT);
        password.sendKeys(Keys.ENTER);
        return this;
    }

    @Step("Click quên mật khẩu")
    public LoginPage clickForgotPassword() {
        click(FORGOT_LINK);
        return this;
    }

    @Step("Click đăng nhập bằng Google")
    public LoginPage clickGoogleEmailLogin() {
        click(GOOGLE_OAUTH);
        return this;
    }

    // ---------------- Queries (chỉ đọc, KHÔNG assert) ----------------

    @Step("Đọc nội dung div.error")
    public String getErrorText() {
        if (!isVisible(ERROR_DIV)) {
            return "";
        }
        return getText(ERROR_DIV).trim();
    }

    @Step("Đọc attribute 'type' của ô password (dùng để verify masking)")
    public String getPasswordFieldType() {
        WebElement pwd = waitForVisible(PASSWORD_INPUT);
        String type = pwd.getDomAttribute("type");
        log.debug("Password input type attribute = '{}'", type);
        return type;
    }

    @Step("Đọc attribute 'maxlength' của ô password (dùng để verify giới hạn độ dài client-side)")
    public String getPasswordMaxLength() {
        WebElement pwd = waitForVisible(PASSWORD_INPUT);
        String maxLength = pwd.getDomAttribute("maxlength");
        log.debug("Password input maxlength attribute = '{}'", maxLength);
        return maxLength;
    }

    @Step("Đọc trạng thái Remember Me (luôn false: checkbox không còn trên trang UTC)")
    public boolean isRememberMeSelected() {
        // Trang login UTC hiện tại không có Remember Me → luôn coi như không tích.
        log.debug("isRememberMeSelected() → false (no checkbox on page)");
        return false;
    }

    @Step("Kiểm tra đang ở trang login")
    public boolean isOnLoginPage() {
        return wait(Duration.ofSeconds(3))
                .until(d -> getDriver().getCurrentUrl())
                .contains("/Login");
    }

    /**
     * Chờ URL rời khỏi trang login → chứng tỏ đã navigate sau khi submit.
     * KHÔNG assert — chỉ navigate + đợi.
     */
    @Step("Chờ URL rời khỏi trang login")
    public LoginPage assertLoggedInSuccessfully() {
        wait(Duration.ofSeconds(15))
                .until(d -> !d.getCurrentUrl().contains("/Login"));
        return this;
    }
}
