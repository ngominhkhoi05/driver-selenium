package com.ws.driver_selenium.model;

/**
 * Dữ liệu đầu vào cho 1 case đăng nhập. Dùng {@code record} để
 * giữ cấu trúc bất biến + auto-generate equals/hashCode/toString.
 *
 * @param username   tên đăng nhập (có thể rỗng, có thể là whitespace, có thể là SQLi)
 * @param password   mật khẩu (tương tự username)
 * @param rememberMe có tích Remember Me hay không
 */
public record LoginData(String username, String password, boolean rememberMe) {

    public static LoginData of(String username, String password) {
        return new LoginData(username, password, false);
    }

    public static LoginData of(String username, String password, boolean rememberMe) {
        return new LoginData(username, password, rememberMe);
    }
}
