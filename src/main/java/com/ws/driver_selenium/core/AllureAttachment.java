package com.ws.driver_selenium.core;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

/**
 * Helper đính kèm ảnh chụp màn hình + URL hiện tại vào báo cáo Allure.
 * <p>
 * Mỗi {@code @AfterEach} nên gọi {@link #attachScreenshot(WebDriver, String)} để
 * dù test pass hay fail đều có ảnh đính kèm, giúp debug dễ hơn.
 */
public final class AllureAttachment {

    private AllureAttachment() {
    }

    public static void attachScreenshot(WebDriver driver, String name) {
        if (!(driver instanceof TakesScreenshot takesScreenshot)) {
            return;
        }
        byte[] png = takesScreenshot.getScreenshotAs(OutputType.BYTES);
        Allure.addAttachment(name, "image/png", new ByteArrayInputStream(png), ".png");
    }

    public static void attachCurrentUrl(WebDriver driver, String name) {
        if (driver == null) {
            return;
        }
        try {
            Allure.addAttachment(name, "text/plain", driver.getCurrentUrl());
        } catch (Exception ignored) {
            // Một số driver đã quit thì getCurrentUrl() ném exception; bỏ qua.
        }
    }
}
