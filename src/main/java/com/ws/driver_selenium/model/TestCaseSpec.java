package com.ws.driver_selenium.model;

import java.util.List;

/**
 * Mô tả đầy đủ 1 test case, tương đương một entry trong JSON spec
 * của bản Python cũ. Giúp test có thể log/attach mô tả + steps +
 * input + expected output một cách nhất quán.
 *
 * <p>Thiết kế dùng abstract class + concrete subclass {@link SimpleTestCaseSpec}
 * để giữ API theo đúng plan:
 * <ul>
 *   <li>{@link #getId()}, {@link #getDescription()}, {@link #getSteps()},</li>
 *   <li>{@link #getInput()}, {@link #getExpectedOutput()}.</li>
 * </ul>
 */
public abstract class TestCaseSpec {

    public abstract String getId();

    public abstract String getDescription();

    public abstract List<String> getSteps();

    public abstract LoginData getInput();

    public abstract String getExpectedOutput();

    /**
     * Implement cụ thể, dùng để truyền data vào test mà không cần
     * tự viết lại abstract method từng lần.
     */
    public static TestCaseSpec of(String id,
                                  String description,
                                  List<String> steps,
                                  LoginData input,
                                  String expectedOutput) {
        return new SimpleTestCaseSpec(id, description, steps, input, expectedOutput);
    }

    /** Chuyển spec thành JSON đơn giản để attach Allure. */
    public String toJson() {
        StringBuilder sb = new StringBuilder(256);
        sb.append("{");
        sb.append("\"id\":\"").append(escapeJson(getId())).append("\",");
        sb.append("\"description\":\"").append(escapeJson(getDescription())).append("\",");
        sb.append("\"steps\":[");
        List<String> steps = getSteps();
        for (int i = 0; i < steps.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append("\"").append(escapeJson(steps.get(i))).append("\"");
        }
        sb.append("],");
        LoginData input = getInput();
        sb.append("\"input\":{")
          .append("\"username\":\"").append(escapeJson(input.username())).append("\",")
          .append("\"password\":\"").append(escapeJson(input.password())).append("\",")
          .append("\"rememberMe\":").append(input.rememberMe())
          .append("},");
        sb.append("\"expectedOutput\":\"").append(escapeJson(getExpectedOutput())).append("\"");
        sb.append("}");
        return sb.toString();
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    /** Concrete subclass giữ data immutable. */
    private static final class SimpleTestCaseSpec extends TestCaseSpec {
        private final String id;
        private final String description;
        private final List<String> steps;
        private final LoginData input;
        private final String expectedOutput;

        SimpleTestCaseSpec(String id, String description, List<String> steps,
                           LoginData input, String expectedOutput) {
            this.id = id;
            this.description = description;
            this.steps = List.copyOf(steps);
            this.input = input;
            this.expectedOutput = expectedOutput;
        }

        @Override public String getId()                 { return id; }
        @Override public String getDescription()        { return description; }
        @Override public List<String> getSteps()        { return steps; }
        @Override public LoginData getInput()           { return input; }
        @Override public String getExpectedOutput()     { return expectedOutput; }
    }
}
