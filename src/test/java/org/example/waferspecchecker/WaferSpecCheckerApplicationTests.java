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

    @Test
    public void testMultipleParametersCombination() {
        parameters.add(new WaferSpecCheckerApplication.TestParameter("Voltage_Y3", 2.5));
        parameters.add(new WaferSpecCheckerApplication.TestParameter("Current_Y4", -30.0));
        parameters.add(new WaferSpecCheckerApplication.TestParameter("Voltage_Y6", 4.0));
        parameters.add(new WaferSpecCheckerApplication.TestParameter("Current_Y7", -15.0));

        boolean result = true;
        for (WaferSpecCheckerApplication.TestParameter param : parameters) {
            result &= WaferSpecCheckerApplication.checkParameterSpec(param);
        }
        assertTrue(result);
    }

    @Test
    public void testParameterNameNotVoltageOrCurrent() {
        WaferSpecCheckerApplication.TestParameter param = new WaferSpecCheckerApplication.TestParameter("Resistance_R1", 100.0);
        assertTrue(WaferSpecCheckerApplication.checkParameterSpec(param));
    }

    @Test
    public void testExtremeValues() {
        WaferSpecCheckerApplication.TestParameter voltageExtremeLow = new WaferSpecCheckerApplication.TestParameter("Voltage_Y3", Double.MIN_VALUE);
        WaferSpecCheckerApplication.TestParameter voltageExtremeHigh = new WaferSpecCheckerApplication.TestParameter("Voltage_Y3", Double.MAX_VALUE);
        WaferSpecCheckerApplication.TestParameter currentExtremeLow = new WaferSpecCheckerApplication.TestParameter("Current_Y4", Double.MIN_VALUE);
        WaferSpecCheckerApplication.TestParameter currentExtremeHigh = new WaferSpecCheckerApplication.TestParameter("Current_Y4", Double.MAX_VALUE);

        assertFalse(WaferSpecCheckerApplication.checkParameterSpec(voltageExtremeLow));
        assertFalse(WaferSpecCheckerApplication.checkParameterSpec(voltageExtremeHigh));
        assertFalse(WaferSpecCheckerApplication.checkParameterSpec(currentExtremeLow));
        assertFalse(WaferSpecCheckerApplication.checkParameterSpec(currentExtremeHigh));
    }

    @Test
    public void testParameterNameCaseSensitivity() {
        WaferSpecCheckerApplication.TestParameter voltageLowerCase = new WaferSpecCheckerApplication.TestParameter("voltage_y3", 3.0);
        WaferSpecCheckerApplication.TestParameter currentLowerCase = new WaferSpecCheckerApplication.TestParameter("current_y4", -20.0);
        WaferSpecCheckerApplication.TestParameter voltageMixedCase = new WaferSpecCheckerApplication.TestParameter("VoLtAgE_Y3", 3.5);

        assertTrue(WaferSpecCheckerApplication.checkParameterSpec(voltageLowerCase)); // 修正後忽略大小寫，應通過
        assertTrue(WaferSpecCheckerApplication.checkParameterSpec(currentLowerCase)); // 修正後忽略大小寫，應通過
        assertTrue(WaferSpecCheckerApplication.checkParameterSpec(voltageMixedCase)); // 包含"voltage"，應通過
    }

    @Test
    public void testLargeNumberOfParameters() {
        for (int i = 0; i < 100; i++) {
            parameters.add(new WaferSpecCheckerApplication.TestParameter("Voltage_Y" + i, 3.0));
            parameters.add(new WaferSpecCheckerApplication.TestParameter("Current_Y" + i, -20.0));
        }

        boolean result = true;
        for (WaferSpecCheckerApplication.TestParameter param : parameters) {
            result &= WaferSpecCheckerApplication.checkParameterSpec(param);
        }
        assertTrue(result);
    }

    @Test
    public void testNegativeBoundaryValues() {
        WaferSpecCheckerApplication.TestParameter voltageNegative = new WaferSpecCheckerApplication.TestParameter("Voltage_Y3", -2.3);
        WaferSpecCheckerApplication.TestParameter currentJustAboveMin = new WaferSpecCheckerApplication.TestParameter("Current_Y4", -39.9);

        assertFalse(WaferSpecCheckerApplication.checkParameterSpec(voltageNegative));
        assertTrue(WaferSpecCheckerApplication.checkParameterSpec(currentJustAboveMin));
    }

    @Test
    public void testZeroValues() {
        WaferSpecCheckerApplication.TestParameter voltageZero = new WaferSpecCheckerApplication.TestParameter("Voltage_Y3", 0.0);
        WaferSpecCheckerApplication.TestParameter currentZero = new WaferSpecCheckerApplication.TestParameter("Current_Y4", 0.0);

        assertFalse(WaferSpecCheckerApplication.checkParameterSpec(voltageZero));
        assertFalse(WaferSpecCheckerApplication.checkParameterSpec(currentZero));
    }

    @Test
    public void testEmptyOrNullParameterName() {
        WaferSpecCheckerApplication.TestParameter emptyName = new WaferSpecCheckerApplication.TestParameter("", 3.0);
        assertTrue(WaferSpecCheckerApplication.checkParameterSpec(emptyName)); // 預設通過，因為不包含Voltage或Current
    }
}
