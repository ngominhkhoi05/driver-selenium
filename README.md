# driver-selenium

Dự án kiểm thử tự động (automation testing) cho chức năng **Đăng nhập** của hệ thống
Văn phòng điện tử UTC — sử dụng **Selenium 4**, **JUnit 5** và **Allure Report**.

> **Hệ thống kiểm thử**: [https://vanphongdientu.utc.edu.vn/Login](https://vanphongdientu.utc.edu.vn/Login)

---

## Thông tin sinh viên

| Mục | Nội dung |
| --- | --- |
| Họ tên | Ngô Minh Khôi |
| MSSV | 6451071037 |
| Học phần | Kiểm thử Phần mềm |
| Trường | Đại học Giao thông vận tải TP.HCM (UTC) |

---

## Mục lục

- [1. Giới thiệu](#1-giới-thiệu)
- [2. Công nghệ sử dụng](#2-công-nghệ-sử-dụng)
- [3. Yêu cầu môi trường](#3-yêu-cầu-môi-trường)
- [4. Cài đặt](#4-cài-đặt)
- [5. Cách chạy test](#5-cách-chạy-test)
- [6. Allure Report](#6-allure-report)
- [7. Cấu trúc dự án](#7-cấu-trúc-dự-án)
- [8. Danh sách 20 test case](#8-danh-sách-20-test-case)
- [9. Ghi chú](#9-ghi-chú)

---

## 1. Giới thiệu

Dự án thực hiện kiểm thử tự động **20 test case** cho form đăng nhập của hệ thống
Văn phòng điện tử UTC, bao gồm:

- Validation (trống, sai, khoảng trắng).
- Đăng nhập thành công với / không có Remember Me.
- Bảo mật (case-sensitive password, password masking, SQL injection).
- Điều hướng (link Quên mật khẩu, OAuth Google, refresh, logout).
- Edge case (password dài 300 ký tự, không tồn tại username).

Mục tiêu: phát hiện sớm regression, tăng độ tin cậy cho chức năng đăng nhập.

## 2. Công nghệ sử dụng

| Thành phần | Phiên bản | Mục đích |
| --- | --- | --- |
| Java | 21 (Temurin) | Ngôn ngữ lập trình |
| Gradle | 9.x | Build tool (qua wrapper) |
| Selenium | 4.x | Web automation (ChromeDriver tự tải) |
| JUnit 5 | 5.x | Test framework |
| AssertJ | 3.x | Fluent assertions |
| Allure | 2.35.5 | Test report (Epic/Feature/Story/Severity + screenshot) |
| Spring Boot | 4.x | Skeleton (context load cho smoke test) |

## 3. Yêu cầu môi trường

- **JDK 21** (Temurin / Adoptium khuyến nghị).
- **Google Chrome** (bản ổn định mới nhất) — Selenium Manager tự tải ChromeDriver tương ứng.
- Kết nối Internet (để ChromeDriver tự tải lần đầu).

## 4. Cài đặt

### 4.1. Cài JDK 21

Tải và cài đặt **Temurin 21** từ [Adoptium](https://adoptium.net/).
Kiểm tra sau khi cài:

```bash
java -version
# openjdk version "21.0.x" ...
```

Đặt biến môi trường `JAVA_HOME` trỏ về thư mục JDK 21 (nếu cần cho Gradle toolchain).

### 4.2. Clone project

```bash
git clone https://github.com/ngominhkhoi05/driver-selenium.git
cd driver-selenium
```

### 4.3. Cấp quyền cho Gradle wrapper (Linux / macOS)

```bash
chmod +x gradlew
```

## 5. Cách chạy test

### 5.1. Chạy toàn bộ test (headed, có Chrome UI)

```bash
./gradlew test
```

### 5.2. Chạy headless (CI / server không có display)

```bash
./gradlew test -Dheadless=true
```

### 5.3. Chạy một test class cụ thể

```bash
./gradlew test --tests "com.ws.driver_selenium.tests.login.TC05_LoginSuccessWithRememberMeTest"
```

### 5.4. Chạy nhiều test theo pattern

```bash
./gradlew test --tests "com.ws.driver_selenium.tests.login.TC1*"
```

### 5.5. Xem log chi tiết

```bash
./gradlew test --info
```

Sau khi chạy, kết quả HTML ở `build/reports/tests/test/index.html`
và Allure raw data ở `build/allure-results/`.

## 6. Allure Report

Project dùng **Allure JUnit5** để sinh JSON kết quả vào `build/allure-results/`.
Để xem report HTML đẹp:

```bash
# Cài Allure CLI (một lần)
# macOS:    brew install allure
# Windows:  scoop install allure
# Linux:    xem https://allurereport.org/docs/gettingstarted/

# Serve report từ kết quả đã sinh
allure serve build/allure-results
```

Report sẽ tự động mở trong trình duyệt, bao gồm:

- **Overview** — biểu đồ passed/failed/broken/skipped.
- **Categories** — phân loại lỗi.
- **Suites** — danh sách test theo package.
- **Graphs** — biểu đồ thời gian, retry.
- **Timeline** — trình tự test.
- **Behaviors** — phân nhóm theo Epic / Feature / Story.
- **Packages** — phân nhóm theo Java package.

Mỗi test có:
- **Screenshot PNG** với tên `Screenshot [PASSED|FAILED] - TCxx_<Ten>`.
- **URL hiện tại** sau khi test kết thúc.
- **Dữ liệu input** (JSON) đính kèm qua `Allure.addAttachment`.

**Environment tab** trong report hiển thị 8 thuộc tính do `EnvironmentWriter`
extension tự sinh:

```
Sinh_Vien=Ngo Minh Khoi
MSSV=6451071037
Hoc_Phan=Kiem thu phan mem
He_Thong_Kiem_Thu=Van phong dien tu UTC (https://vanphongdientu.utc.edu.vn/Login)
Trinh_Duyet=Google Chrome
Framework=Selenium 4 + JUnit 5 + Allure Report
Java_Version=21.x
Gradle_Version=21.x.x
```

## 7. Cấu trúc dự án

```
driver-selenium/
├── build.gradle                       # Gradle config (Selenium + JUnit 5 + Allure)
├── gradlew, gradlew.bat, gradle/      # Gradle wrapper
├── src/
│   ├── main/
│   │   ├── java/com/ws/driver_selenium/
│   │   │   ├── core/                  # DriverFactory, DriverManager, AllureAttachment
│   │   │   ├── model/                 # LoginData, TestCaseSpec
│   │   │   ├── data/                  # HardcodedTestCases (20 TC)
│   │   │   ├── constants/             # LoginErrorMessages, Url
│   │   │   └── pages/                 # LoginPage (Page Object)
│   │   └── resources/
│   │       ├── application.yaml       # Spring config
│   │       └── allure.properties      # Allure config
│   └── test/
│       ├── java/com/ws/driver_selenium/tests/
│       │   ├── base/BaseTest.java                  # Base cho mỗi test
│       │   ├── hooks/
│       │   │   ├── AllureScreenshotExtension.java  # Auto screenshot
│       │   │   └── EnvironmentWriter.java          # Auto ghi env properties
│       │   ├── login/TC01_...TC20_*.java           # 20 test class
│       │   ├── smoke/                              # Smoke test
│       │   └── data/                               # Unit test cho hardcoded data
│       └── resources/
│           ├── allure-test.properties              # Override env (mặc định rỗng)
│           ├── junit-platform.properties           # Enable JUnit extension autodetection
│           └── META-INF/services/.../Extension     # SPI: auto-register extension
└── README.md                          # File này
```

## 8. Danh sách 20 test case

Toàn bộ test nằm trong package `com.ws.driver_selenium.tests.login`.

| ID | Mô tả | Status khi nộp |
| --- | --- | --- |
| TC01 | Bỏ trống username | ✅ PASS |
| TC02 | Bỏ trống password | ✅ PASS |
| TC03 | Sai mật khẩu với username hợp lệ | ✅ PASS |
| TC04 | Username không tồn tại trong hệ thống | ✅ PASS |
| TC05 | Đăng nhập thành công khi tích Remember Me | ⏸ SKIPPED (cần credential thật) |
| TC06 | Đăng nhập thành công khi KHÔNG tích Remember Me | ⏸ SKIPPED (cần credential thật) |
| TC07 | Cả username và password đều trống | ✅ PASS |
| TC08 | Cả username và password đều sai | ✅ PASS |
| TC09 | Username chỉ chứa khoảng trắng | ✅ PASS |
| TC10 | Password chỉ chứa khoảng trắng | ✅ PASS |
| TC11 | Password đảo case → báo INVALID_CREDENTIALS | ✅ PASS |
| TC12 | Đăng nhập thành công bằng phím Enter | ⏸ SKIPPED (cần credential thật) |
| TC13 | Ô password có type='password' (mask ký tự) | ✅ PASS |
| TC14 | Username không tồn tại → báo INVALID_CREDENTIALS | ✅ PASS |
| TC15 | Password 300 ký tự → báo INVALID_CREDENTIALS | ✅ PASS |
| TC16 | Click 'Quên mật khẩu' → navigate /Login/GetPass | ✅ PASS |
| TC17 | Click 'Đăng nhập bằng e-mail UTC' → navigate tới accounts.google.com | ✅ PASS |
| TC18 | Refresh trang sau khi login → vẫn ở trạng thái login | ⏸ SKIPPED (cần credential thật) |
| TC19 | Logout sau khi login → URL quay về /Login | ⏸ SKIPPED (cần credential thật) |
| TC20 | Username chứa payload SQL injection → báo INVALID_CREDENTIALS | ✅ PASS |

**Tổng kết**: 15 PASS, 5 SKIPPED (đều là happy path cần credential thật, có annotation `@Disabled`).

## 9. Ghi chú

- **5 test bị `@Disabled`** (TC05, TC06, TC12, TC18, TC19) đều cần tài khoản UTC thật
  để verify. Khi có tài khoản, bỏ annotation `@Disabled` trên class tương ứng.
- **TC17** trigger OAuth Google flow nhưng Google trả về `Error 400: redirect_uri_mismatch`
  (cấu hình app UTC, không phải bug test). Test verify URL chứa `accounts.google.com`.
- **TC19** cần bổ sung method `LoginPage.clickLogout()` trước khi bật lại.
- Tất cả test chạy với **Chrome thật** (không dùng ChromeDriver cố định — Selenium 4
  tự tải version phù hợp).
- Project skeleton Spring Boot được giữ lại để `DriverSeleniumApplicationTests.contextLoads()`
  có thể kiểm tra Spring context — không dùng cho chức năng đăng nhập.

---

**Công nghệ**: Selenium 4 · JUnit 5 · Allure Report · Gradle · JDK 21
