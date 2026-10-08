package com.ws.driver_selenium.tests.smoke;

import com.ws.driver_selenium.data.HardcodedTestCases;
import com.ws.driver_selenium.model.LoginData;
import com.ws.driver_selenium.model.TestCaseSpec;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test thuần cho Phase 4: verify cấu trúc {@link HardcodedTestCases}
 * (không cần Selenium, không cần network).
 * <p>
 * DoD của plan: <em>"Có thể gọi HardcodedTestCases.TC3_WRONG_PASSWORD.getInput()
 * mà không cần đọc JSON."</em>
 */
class HardcodedTestCasesTest {

    @Test
    @DisplayName("TC3_WRONG_PASSWORD.getInput() trả về username/password đúng")
    void tc3ShouldExposeInput() {
        LoginData input = HardcodedTestCases.TC3_WRONG_PASSWORD.getInput();

        assertThat(input.username()).isEqualTo("huongnt");
        assertThat(input.password()).isEqualTo("123456@utc");
        assertThat(input.rememberMe()).isFalse();
    }

    @Test
    @DisplayName("Tất cả 20 case đều có id, description, input, expectedOutput")
    void allCasesAreWellFormed() {
        assertThat(HardcodedTestCases.ALL)
                .as("Có đúng 20 test case")
                .hasSize(20)
                .allSatisfy(spec -> {
                    assertThat(spec.getId()).matches("TC\\d{1,2}");
                    assertThat(spec.getDescription()).isNotBlank();
                    assertThat(spec.getInput()).isNotNull();
                    assertThat(spec.getExpectedOutput()).isNotBlank();
                    assertThat(spec.getSteps()).isNotEmpty();
                });
    }

    @Test
    @DisplayName("TestCaseSpec.toJson() sinh JSON hợp lệ chứa username/password")
    void toJsonShouldContainFields() {
        TestCaseSpec tc = HardcodedTestCases.TC5_LOGIN_SUCCESS_WITH_REMEMBER;

        String json = tc.toJson();

        assertThat(json)
                .contains("\"id\":\"TC5\"")
                .contains("\"rememberMe\":true")
                .contains("\"username\":\"huongnt\"")
                .contains("\"password\":\"123456@utc\"");
    }

    @Test
    @DisplayName("TC11 đảo case password (123456@UTC) khác TC3 (123456@utc)")
    void tc11PasswordShouldBeSwapCased() {
        assertThat(HardcodedTestCases.TC11_PASSWORD_CASE_SENSITIVE.getInput().password())
                .isEqualTo("123456@UTC");
        assertThat(HardcodedTestCases.TC3_WRONG_PASSWORD.getInput().password())
                .isEqualTo("123456@utc");
    }

    @Test
    @DisplayName("TC15 password dài 300 ký tự 'a'")
    void tc15PasswordShouldBeLong() {
        assertThat(HardcodedTestCases.TC15_LONG_PASSWORD.getInput().password())
                .hasSize(300)
                .matches("a{300}");
    }

    @Test
    @DisplayName("TC20 SQL injection: username chứa quote")
    void tc20ShouldCarrySqlInjectionPayload() {
        assertThat(HardcodedTestCases.TC20_SQL_INJECTION.getInput().username())
                .isEqualTo("' OR '1'='1");
    }
}
