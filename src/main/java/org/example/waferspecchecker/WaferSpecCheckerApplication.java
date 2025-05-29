package org.example.waferspecchecker;

import java.util.ArrayList;
import java.util.List;

public class WaferSpecCheckerApplication {
    private static final double VOLTAGE_MIN = 2.3;
    private static final double VOLTAGE_MAX = 4.1;
    private static final double CURRENT_MIN = -40.0;
    private static final double CURRENT_MAX = -10.0;

    static class TestParameter {
        String name;
        double value;

        TestParameter(String name, double value) {
            this.name = name;
            this.value = value;
        }
    }

    public static void main(String[] args) {
        List<TestParameter> parameters = new ArrayList<>();
        parameters.add(new TestParameter("Voltage_Y3", 3.0));
        parameters.add(new TestParameter("Current_Y4", -20.0));
        parameters.add(new TestParameter("Threshold_Y5", 1.2));

        analyzeTestResults(parameters);
    }

    public static void analyzeTestResults(List<TestParameter> parameters) {
        System.out.println("晶圓測試結果分析：");
        boolean isPass = true;

        for (TestParameter param : parameters) {
            boolean withinSpec = checkParameterSpec(param);
            if (!withinSpec) {
                isPass = false;
                System.out.println("異常參數 - " + param.name + ": " + param.value + " (超出規格範圍)");
            } else {
                System.out.println("正常參數 - " + param.name + ": " + param.value + " (符合規格)");
            }
        }

        if (isPass) {
            System.out.println("晶圓測試結果：通過 (良率正常)");
        } else {
            System.out.println("晶圓測試結果：未通過 (良率異常，需進一步分析)");
        }
    }

    public static boolean checkParameterSpec(TestParameter param) {
        if (param.name.contains("Voltage")) {
            return param.value >= VOLTAGE_MIN && param.value <= VOLTAGE_MAX;
        } else if (param.name.contains("Current")) {
            return param.value >= CURRENT_MIN && param.value <= CURRENT_MAX;
        }
        return true;
    }
}
