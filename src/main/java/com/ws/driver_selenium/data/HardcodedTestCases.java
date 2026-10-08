package com.ws.driver_selenium.data;

import com.ws.driver_selenium.model.LoginData;
import com.ws.driver_selenium.model.TestCaseSpec;

import java.util.List;

import static com.ws.driver_selenium.model.LoginData.of;

/**
 * Bộ test case cứng (TC1..TC20) thay thế cho JSON TC cũ của bản Python.
 * <p>
 * Mục đích: có thể gọi trực tiếp trong test, ví dụ:
 * <pre>{@code
 * TestCaseSpec tc = HardcodedTestCases.TC3_WRONG_PASSWORD;
 * loginPage.open()
 *          .enterUsername(tc.getInput().username())
 *          .enterPassword(tc.getInput().password())
 *          .clickLogin();
 * assertThat(loginPage.getErrorText()).contains(tc.getExpectedOutput());
 * }</pre>
 * <p>
 * Một số case (TC16, TC17, TC18, TC19) không có username/password vì chỉ
 * là hành động click/refresh/logout. Vẫn tạo {@link LoginData} rỗng để giữ
 * API đồng nhất.
 */
public final class HardcodedTestCases {

    private HardcodedTestCases() {
    }

    // ====================================================================
    // TC1..TC5 – Validation + happy path có remember
    // ====================================================================

    public static final TestCaseSpec TC1_EMPTY_USERNAME = TestCaseSpec.of(
            "TC1",
            "Để trống username → báo lỗi EMPTY_USERNAME",
            List.of("Mở trang login", "Để trống username", "Nhập password hợp lệ", "Click Login"),
            of("", "123456@utc", false),
            "EMPTY_USERNAME"
    );

    public static final TestCaseSpec TC2_EMPTY_PASSWORD = TestCaseSpec.of(
            "TC2",
            "Để trống password → báo lỗi EMPTY_PASSWORD",
            List.of("Mở trang login", "Nhập username hợp lệ", "Để trống password", "Click Login"),
            of("huongnt", "", false),
            "EMPTY_PASSWORD"
    );

    public static final TestCaseSpec TC3_WRONG_PASSWORD = TestCaseSpec.of(
            "TC3",
            "Sai mật khẩu với username hợp lệ",
            List.of("Mở trang login", "Nhập username hợp lệ", "Nhập sai password", "Click Login"),
            of("huongnt", "123456@utc", false),
            "Sai mật khẩu"
    );

    public static final TestCaseSpec TC4_WRONG_USERNAME = TestCaseSpec.of(
            "TC4",
            "Username không tồn tại → báo INVALID_CREDENTIALS",
            List.of("Mở trang login", "Nhập username không tồn tại", "Nhập password", "Click Login"),
            of("user_invalid", "123456@utc", false),
            "INVALID_CREDENTIALS"
    );

    public static final TestCaseSpec TC5_LOGIN_SUCCESS_WITH_REMEMBER = TestCaseSpec.of(
            "TC5",
            "Đăng nhập thành công khi tích Remember Me",
            List.of("Mở trang login", "Nhập username/password hợp lệ", "Tích Remember Me", "Click Login"),
            of("huongnt", "123456@utc", true),
            "LOGIN_SUCCESS"
    );

    // ====================================================================
    // TC6..TC10 – Whitespace + both-empty + both-wrong + login bằng Enter
    // ====================================================================

    public static final TestCaseSpec TC6_LOGIN_SUCCESS_WITHOUT_REMEMBER = TestCaseSpec.of(
            "TC6",
            "Đăng nhập thành công khi KHÔNG tích Remember Me",
            List.of("Mở trang login", "Nhập username/password hợp lệ", "Không tích Remember Me", "Click Login"),
            of("huongnt", "123456@utc", false),
            "LOGIN_SUCCESS"
    );

    public static final TestCaseSpec TC7_BOTH_FIELDS_EMPTY = TestCaseSpec.of(
            "TC7",
            "Cả username và password đều trống → báo EMPTY_USERNAME",
            List.of("Mở trang login", "Để trống cả 2 field", "Click Login"),
            of("", "", false),
            "EMPTY_USERNAME"
    );

    public static final TestCaseSpec TC8_BOTH_FIELDS_WRONG = TestCaseSpec.of(
            "TC8",
            "Username & password đều sai → báo INVALID_CREDENTIALS",
            List.of("Mở trang login", "Nhập username sai + password sai", "Click Login"),
            of("user_invalid", "123456@utc", false),
            "INVALID_CREDENTIALS"
    );

    public static final TestCaseSpec TC9_USERNAME_ONLY_WHITESPACE = TestCaseSpec.of(
            "TC9",
            "Username chỉ chứa khoảng trắng → báo lỗi",
            List.of("Mở trang login", "Nhập username = '   '", "Nhập password hợp lệ", "Click Login"),
            of("   ", "123456@utc", false),
            "EMPTY_USERNAME"
    );

    public static final TestCaseSpec TC10_PASSWORD_ONLY_WHITESPACE = TestCaseSpec.of(
            "TC10",
            "Password chỉ chứa khoảng trắng → báo lỗi",
            List.of("Mở trang login", "Nhập username hợp lệ", "Nhập password = '   '", "Click Login"),
            of("huongnt", "   ", false),
            "EMPTY_PASSWORD"
    );

    // ====================================================================
    // TC11..TC15 – Password case-sensitive, masking, long password
    // ====================================================================

    public static final TestCaseSpec TC11_PASSWORD_CASE_SENSITIVE = TestCaseSpec.of(
            "TC11",
            "Password phân biệt hoa/thường (đảo case) → báo lỗi",
            List.of("Mở trang login", "Nhập username hợp lệ", "Nhập password đảo case", "Click Login"),
            of("huongnt", "123456@UTC", false),   // swapcase("123456@utc")
            "INVALID_CREDENTIALS"
    );

    public static final TestCaseSpec TC12_LOGIN_WITH_ENTER_KEY = TestCaseSpec.of(
            "TC12",
            "Đăng nhập thành công bằng phím Enter (không cần click)",
            List.of("Mở trang login", "Nhập username/password hợp lệ", "Nhấn phím Enter trong ô password"),
            of("huongnt", "123456@utc", false),
            "LOGIN_SUCCESS"
    );

    public static final TestCaseSpec TC13_PASSWORD_MASKING = TestCaseSpec.of(
            "TC13",
            "Ô password phải có type='password' (mask ký tự)",
            List.of("Mở trang login", "Nhập password bất kỳ", "Kiểm tra attribute type của input"),
            of("huongnt", "123456@utc", false),
            "PASSWORD_MASKED"
    );

    public static final TestCaseSpec TC14_NONEXISTENT_USERNAME = TestCaseSpec.of(
            "TC14",
            "Username không tồn tại trong hệ thống → báo lỗi",
            List.of("Mở trang login", "Nhập username không có thật", "Nhập password", "Click Login"),
            of("nonexistent_user_12345", "123456", false),
            "INVALID_CREDENTIALS"
    );

    public static final TestCaseSpec TC15_LONG_PASSWORD = TestCaseSpec.of(
            "TC15",
            "Password rất dài (300 ký tự 'a') → báo lỗi boundary",
            List.of("Mở trang login", "Nhập username hợp lệ", "Nhập password 300 ký tự", "Click Login"),
            of("huongnt", "a".repeat(300), false),
            "INVALID_CREDENTIALS"
    );

    // ====================================================================
    // TC16..TC19 – Hành động đặc biệt: forgot password / Google OAuth /
    //              refresh sau login / logout
    // (không có input username/password — chỉ có action)
    // ====================================================================

    public static final TestCaseSpec TC16_FORGOT_PASSWORD = TestCaseSpec.of(
            "TC16",
            "Click 'Quên mật khẩu' → điều hướng đến /Login/GetPass",
            List.of("Mở trang login", "Click vào link 'Quên mật khẩu'"),
            LoginData.of("", ""),     // không dùng input
            "NAVIGATE_GETPASS"
    );

    public static final TestCaseSpec TC17_GOOGLE_OAUTH = TestCaseSpec.of(
            "TC17",
            "Click 'Đăng nhập bằng e-mail UTC' → điều hướng OAuth Google",
            List.of("Mở trang login", "Click nút đăng nhập Google"),
            LoginData.of("", ""),
            "NAVIGATE_GOOGLE_OAUTH"
    );

    public static final TestCaseSpec TC18_REFRESH_AFTER_LOGIN = TestCaseSpec.of(
            "TC18",
            "Sau khi login thành công → refresh trang → vẫn ở trạng thái login",
            List.of("Đăng nhập thành công", "Reload trang", "Kiểm tra vẫn còn session"),
            of("huongnt", "123456@utc", false),
            "STILL_LOGGED_IN"
    );

    public static final TestCaseSpec TC19_LOGOUT = TestCaseSpec.of(
            "TC19",
            "Login rồi logout → quay về trang /Login",
            List.of("Đăng nhập thành công", "Click nút Logout", "Kiểm tra URL về /Login"),
            of("huongnt", "123456@utc", false),
            "REDIRECT_TO_LOGIN"
    );

    // ====================================================================
    // TC20 – SQL injection
    // ====================================================================

    public static final TestCaseSpec TC20_SQL_INJECTION = TestCaseSpec.of(
            "TC20",
            "Username chứa payload SQL injection → báo lỗi (không bị bypass)",
            List.of("Mở trang login", "Nhập username SQLi", "Nhập password bất kỳ", "Click Login"),
            of("' OR '1'='1", "123456", false),
            "INVALID_CREDENTIALS"
    );

    // ====================================================================
    // Tập hợp tất cả 20 case (tiện cho tham chiếu / report)
    // ====================================================================

    public static final List<TestCaseSpec> ALL = List.of(
            TC1_EMPTY_USERNAME,
            TC2_EMPTY_PASSWORD,
            TC3_WRONG_PASSWORD,
            TC4_WRONG_USERNAME,
            TC5_LOGIN_SUCCESS_WITH_REMEMBER,
            TC6_LOGIN_SUCCESS_WITHOUT_REMEMBER,
            TC7_BOTH_FIELDS_EMPTY,
            TC8_BOTH_FIELDS_WRONG,
            TC9_USERNAME_ONLY_WHITESPACE,
            TC10_PASSWORD_ONLY_WHITESPACE,
            TC11_PASSWORD_CASE_SENSITIVE,
            TC12_LOGIN_WITH_ENTER_KEY,
            TC13_PASSWORD_MASKING,
            TC14_NONEXISTENT_USERNAME,
            TC15_LONG_PASSWORD,
            TC16_FORGOT_PASSWORD,
            TC17_GOOGLE_OAUTH,
            TC18_REFRESH_AFTER_LOGIN,
            TC19_LOGOUT,
            TC20_SQL_INJECTION
    );
}
