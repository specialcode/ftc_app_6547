package com.qualcomm.ftcrobotcontroller.hardware;

import com.qualcomm.ftcrobotcontroller.hardware.nullware.NullGyro;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by STACK0V3RFL0W on 3/5/2016.
 */
public class GyroPIDControllerTest extends TestCase {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetMotorDirection() throws Exception {
        GyroPIDController tested = new GyroPIDController(new NullGyro());
        tested.setDirection(GyroPIDController.gyroMotorDirection.Forward);
        assertEquals(GyroPIDController.gyroMotorDirection.Forward, tested.getMotorDirection());

    }

    @Test
    public void testSetDirection() throws Exception {
        GyroPIDController tested = new GyroPIDController(new NullGyro());
        tested.setDirection(GyroPIDController.gyroMotorDirection.Forward);
        assertEquals(GyroPIDController.gyroMotorDirection.Forward, tested.getMotorDirection());

    }

    @Test
    public void testSetTargetAngle() throws Exception {
        GyroPIDController tested = new GyroPIDController(new NullGyro());
        tested.setTargetAngle(0);
        tested.setDirection(GyroPIDController.gyroMotorDirection.Forward);
        assertEquals(0.0, tested.getTarget());

    }

    @Test
    public void testInitialize() throws Exception {
        GyroPIDController tested = new GyroPIDController(new NullGyro());
        tested.initialize();
        assertEquals(0.0, tested.getCurrent());


    }

    @Test
    public void testCalculateMotorPowers_WhenGyroIS_Zero() throws Exception {
        NullGyro mockGyro = new NullGyro();
        GyroPIDController tested = new GyroPIDController(mockGyro);
        tested.setDirection(GyroPIDController.gyroMotorDirection.Reverse);
        tested.initialize();
        mockGyro.setHeading(0);
        tested.calculateMotorPowers();

        assertEquals(-0.5, tested.getLeftPower());
        assertEquals(-0.5, tested.getRightPower());
    }

    @Test
    public void testCalculateMotorPowers_WhenGyroIS_ONE() throws Exception {
        NullGyro mockGyro = new NullGyro();
        GyroPIDController tested = new GyroPIDController(mockGyro);
        tested.setDirection(GyroPIDController.gyroMotorDirection.Reverse);
        tested.initialize();
        mockGyro.setHeading(1);
        tested.calculateMotorPowers();

        assertEquals(-0.65, tested.getLeftPower());
        assertEquals(-0.35, tested.getRightPower());
    }

    @Test
    public void testCalculateMotorPowers_WhenGyroIS_359() throws Exception {
        NullGyro mockGyro = new NullGyro();
        GyroPIDController tested = new GyroPIDController(mockGyro);
        tested.setDirection(GyroPIDController.gyroMotorDirection.Reverse);
        tested.initialize();
        mockGyro.setHeading(359);
        tested.calculateMotorPowers();

        assertEquals(-0.35, tested.getLeftPower());
        assertEquals(-0.65, tested.getRightPower());
    }

    @Test
    public void testCalculateMotorPowers_WhenGyroIS_358() throws Exception {
        NullGyro mockGyro = new NullGyro();
        GyroPIDController tested = new GyroPIDController(mockGyro);
        tested.setDirection(GyroPIDController.gyroMotorDirection.Reverse);
        tested.initialize();
        mockGyro.setHeading(358);
        tested.calculateMotorPowers();

        assertEquals(-0.20, tested.getLeftPower());
        assertEquals(-0.80, tested.getRightPower());
    }

    @Test
    public void testCalculateMotorPowers_WhenGyroIS_357() throws Exception {
        NullGyro mockGyro = new NullGyro();
        GyroPIDController tested = new GyroPIDController(mockGyro);
        tested.setDirection(GyroPIDController.gyroMotorDirection.Reverse);
        tested.initialize();
        mockGyro.setHeading(358);
        tested.calculateMotorPowers();

        assertEquals(-0.20, tested.getLeftPower());
        assertEquals(-0.80, tested.getRightPower());
    }

    @Test
    public void testCalculateMotorPowers_WhenGyroIS_TEN() throws Exception {
        NullGyro mockGyro = new NullGyro();
        GyroPIDController tested = new GyroPIDController(mockGyro);
        tested.setDirection(GyroPIDController.gyroMotorDirection.Reverse);
        tested.initialize();
        mockGyro.setHeading(10);
        tested.calculateMotorPowers();

        assertEquals(-0.8, tested.getLeftPower());
        assertEquals(0.8, tested.getRightPower());
    }

    @Test
    public void testCleanPower() throws Exception {

        GyroPIDController tested = new GyroPIDController(new NullGyro());
        tested.setDirection(GyroPIDController.gyroMotorDirection.Reverse);
        double testedValue = tested.cleanPower(2);
        assertEquals(0.8, testedValue);
    }
}