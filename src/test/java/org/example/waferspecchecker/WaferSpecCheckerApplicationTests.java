package org.example.waferspecchecker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class WaferSpecCheckerApplicationTests {

    private List<WaferSpecCheckerApplication.TestParameter> parameters;

    @BeforeEach
    public void setUp() {
        parameters = new ArrayList<>();
    }

    @Test
    public void testCheckParameterSpecWithinRange() {
        WaferSpecCheckerApplication.TestParameter voltageParam = new WaferSpecCheckerApplication.TestParameter("Voltage_Y3", 3.5);
        WaferSpecCheckerApplication.TestParameter currentParam = new WaferSpecCheckerApplication.TestParameter("Current_Y4", -15.0);
        WaferSpecCheckerApplication.TestParameter otherParam = new WaferSpecCheckerApplication.TestParameter("Threshold_Y5", 1.2);

        assertTrue(WaferSpecCheckerApplication.checkParameterSpec(voltageParam));
        assertTrue(WaferSpecCheckerApplication.checkParameterSpec(currentParam));
        assertTrue(WaferSpecCheckerApplication.checkParameterSpec(otherParam));
    }

    @Test
    public void testCheckParameterSpecOutOfRange() {
        WaferSpecCheckerApplication.TestParameter voltageParamFail = new WaferSpecCheckerApplication.TestParameter("Voltage_Y3", 5.0);
        WaferSpecCheckerApplication.TestParameter currentParamFail = new WaferSpecCheckerApplication.TestParameter("Current_Y4", -50.0);

        assertFalse(WaferSpecCheckerApplication.checkParameterSpec(voltageParamFail));
        assertFalse(WaferSpecCheckerApplication.checkParameterSpec(currentParamFail));
    }

    @Test
    public void testCheckParameterSpecBoundaryValues() {
        WaferSpecCheckerApplication.TestParameter voltageMin = new WaferSpecCheckerApplication.TestParameter("Voltage_Y3", 2.3);
        WaferSpecCheckerApplication.TestParameter voltageMax = new WaferSpecCheckerApplication.TestParameter("Voltage_Y3", 4.1);
        WaferSpecCheckerApplication.TestParameter currentMin = new WaferSpecCheckerApplication.TestParameter("Current_Y4", -40.0);
        WaferSpecCheckerApplication.TestParameter currentMax = new WaferSpecCheckerApplication.TestParameter("Current_Y4", -10.0);

        assertTrue(WaferSpecCheckerApplication.checkParameterSpec(voltageMin));
        assertTrue(WaferSpecCheckerApplication.checkParameterSpec(voltageMax));
        assertTrue(WaferSpecCheckerApplication.checkParameterSpec(currentMin));
        assertTrue(WaferSpecCheckerApplication.checkParameterSpec(currentMax));
    }

    @Test
    public void testCheckParameterSpecJustOutsideBoundary() {
        WaferSpecCheckerApplication.TestParameter voltageBelowMin = new WaferSpecCheckerApplication.TestParameter("Voltage_Y3", 2.29);
        WaferSpecCheckerApplication.TestParameter voltageAboveMax = new WaferSpecCheckerApplication.TestParameter("Voltage_Y3", 4.11);
        WaferSpecCheckerApplication.TestParameter currentBelowMin = new WaferSpecCheckerApplication.TestParameter("Current_Y4", -40.1);
        WaferSpecCheckerApplication.TestParameter currentAboveMax = new WaferSpecCheckerApplication.TestParameter("Current_Y4", -9.9);

        assertFalse(WaferSpecCheckerApplication.checkParameterSpec(voltageBelowMin));
        assertFalse(WaferSpecCheckerApplication.checkParameterSpec(voltageAboveMax));
        assertFalse(WaferSpecCheckerApplication.checkParameterSpec(currentBelowMin));
        assertFalse(WaferSpecCheckerApplication.checkParameterSpec(currentAboveMax));
    }

    @Test
    public void testAnalyzeTestResultsAllPass() {
        parameters.add(new WaferSpecCheckerApplication.TestParameter("Voltage_Y3", 3.0));
        parameters.add(new WaferSpecCheckerApplication.TestParameter("Current_Y4", -20.0));

        boolean passResult = true;
        for (WaferSpecCheckerApplication.TestParameter param : parameters) {
            passResult &= WaferSpecCheckerApplication.checkParameterSpec(param);
        }
        assertTrue(passResult);
    }

    @Test
    public void testAnalyzeTestResultsSomeFail() {
        parameters.add(new WaferSpecCheckerApplication.TestParameter("Voltage_Y3", 5.0));
        parameters.add(new WaferSpecCheckerApplication.TestParameter("Current_Y4", -50.0));

        boolean failResult = true;
        for (WaferSpecCheckerApplication.TestParameter param : parameters) {
            failResult &= WaferSpecCheckerApplication.checkParameterSpec(param);
        }
        assertFalse(failResult);
    }

    @Test
    public void testAnalyzeTestResultsEmptyList() {
        boolean passResult = true;
        for (WaferSpecCheckerApplication.TestParameter param : parameters) {
            passResult &= WaferSpecCheckerApplication.checkParameterSpec(param);
        }
        assertTrue(passResult); // 空列表應視為通過，因為無異常
    }

    @Test
    public void testAnalyzeTestResultsMixedParameters() {
        parameters.add(new WaferSpecCheckerApplication.TestParameter("Voltage_Y3", 3.0));
        parameters.add(new WaferSpecCheckerApplication.TestParameter("Current_Y4", -50.0));
        parameters.add(new WaferSpecCheckerApplication.TestParameter("Threshold_Y5", 1.2));

        boolean result = true;
        for (WaferSpecCheckerApplication.TestParameter param : parameters) {
            result &= WaferSpecCheckerApplication.checkParameterSpec(param);
        }
        assertFalse(result); // 因為有一個參數超出範圍
    }
}
