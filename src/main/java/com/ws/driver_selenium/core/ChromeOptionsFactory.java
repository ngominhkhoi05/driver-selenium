package com.ws.driver_selenium.core;

import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Sinh {@link ChromeOptions} cho ChromeDriver.
 * <p>
 * Headless bật/tắt bằng system property {@code headless} (mặc định {@code false})
 * do task {@code test} trong {@code build.gradle} truyền vào.
 * <p>
 * Các flag phổ biến được thêm sẵn để chạy ổn định trong CI (Docker, GitHub Actions):
 * <ul>
 *   <li>{@code --no-sandbox}: cần thiết khi chạy với quyền root trong container.</li>
 *   <li>{@code --disable-dev-shm-usage}: tránh lỗi Chrome crash do {@code /dev/shm} quá nhỏ.</li>
 *   <li>{@code --disable-gpu}: ổn định hơn khi chạy headless trong một số môi trường.</li>
 *   <li>{@code --remote-allow-origins=*}: tương thích Selenium 4.11+ / Chrome 111+.</li>
 * </ul>
 */
public final class ChromeOptionsFactory {

    private static final boolean HEADLESS = Boolean.parseBoolean(
            System.getProperty("headless", "false"));

    private ChromeOptionsFactory() {
    }

    public static ChromeOptions build() {
        ChromeOptions options = new ChromeOptions();

        if (HEADLESS) {
            // "--headless=new" là headless mới của Chrome 109+, ổn định hơn "--headless" cũ.
            options.addArguments("--headless=new");
        }

        options.addArguments(
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-gpu",
                "--remote-allow-origins=*",
                "--window-size=1920,1080",
                "--start-maximized"
        );

        return options;
    }

    public static boolean isHeadless() {
        return HEADLESS;
    }
}
