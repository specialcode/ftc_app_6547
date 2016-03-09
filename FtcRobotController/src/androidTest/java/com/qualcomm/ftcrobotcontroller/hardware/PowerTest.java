package com.qualcomm.ftcrobotcontroller.hardware;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Created by STACK0V3RFL0W on 3/5/2016.
 */
public class PowerTest extends TestCase {

    @Test
    public void clampAboveOne() throws Exception {
        double output;

        output = Power.powerClamp(1.5);
        assertEquals(1.0, output);
    }

    @Test
    public void clampBelowNegativeOne() throws Exception {
        double output;

        output = Power.powerClamp(-1.5);
        assertEquals(-1.0, output);
    }

    @Test
    public void clampPositiveDeciaml() throws Exception {
        double output;

        output = Power.powerClamp(0.5);
        assertEquals(0.5, output);
    }

    @Test
    public void clampNegativeDeciaml() throws Exception {
        double output;

        output = Power.powerClamp(-0.5);
        assertEquals(-0.5, output);
    }

    @Test
    public void clampZero() throws Exception {
        double output;

        output = Power.powerClamp(0);
        assertEquals(0, output);
    }

}