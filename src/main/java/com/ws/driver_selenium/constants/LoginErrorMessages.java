package com.ws.driver_selenium.constants;

/**
 * Các thông báo lỗi dự kiến từ server / client khi đăng nhập UTC.
 * <p>
 * Dùng để so sánh trong assertion (test layer), Page Object chỉ trả về text thô qua
 * {@code LoginPage.getErrorText()}.
 */
public final class LoginErrorMessages {

    public static final String EMPTY_USERNAME = "Bạn chưa nhập tên đăng nhập";
    public static final String EMPTY_PASSWORD = "Bạn chưa nhập mật khẩu";
    public static final String INVALID_CREDENTIALS = "Tài khoản hoặc mật khẩu không đúng.";

    private LoginErrorMessages() {
    }
}
