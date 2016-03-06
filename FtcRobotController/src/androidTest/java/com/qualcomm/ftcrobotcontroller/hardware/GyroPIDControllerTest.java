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
    public void testUsageExample() throws Exception {

        //Create an instance of the GyroPidContorller and pass in a Gyro
        //In an Op Mode you will send in one from the HardwareMap
        GyroPIDController tested = new GyroPIDController(new NullGyro());
        //Set the direction - If you will be driving the Motors in reverse set it to reverse.
        // A new enum was created here as opposed to the one in the motors class so it will not have an unneeded dependency
        tested.setDirection(GyroPIDController.gyroMotorDirection.Reverse);
        //
        assertEquals(GyroPIDController.gyroMotorDirection.Reverse, tested.getMotorDirection());
        // The PIDController must be initialized- this will initialize the gyro.
        tested.initialize();
        assertTrue(tested.getIsInitialized());
        //The initialize will wait until the Gyro reads Zero and set the Current property to the Gyro heading
        // calculate will compare the gyro reading to the target and return the recomended values for left and right wheels
        // the values will be between -0.8 and .08
        tested.calculateMotorPowers();
        assertEquals(-.50, tested.getLeftPower());
        assertEquals(-.50, tested.getRightPower());
        //As long as it is on target - in this case 0 it will return .50 for power
        //You can call get curent to see what the gyro reads for print out or logging.
        assertEquals(0, tested.getCurrent());

    }

    @Test
    public void testCallingCalculateBeforeInitializeIsBad() throws Exception {
        boolean errorThrown = false;
        //Create an instance of the GyroPidContorller and pass in a Gyro
        //In an Op Mode you will send in one from the HardwareMap
        GyroPIDController tested = new GyroPIDController(new NullGyro());
        //Set the direction - If you will be driving the Motors in reverse set it to reverse.
        // A new enum was created here as opposed to the one in the motors class so it will not have an unneeded dependency
        tested.setDirection(GyroPIDController.gyroMotorDirection.Reverse);
        assertEquals(GyroPIDController.gyroMotorDirection.Reverse, tested.getMotorDirection());
        // The PIDController must be initialized- this will initialize the gyro.
        try {
            tested.calculateMotorPowers();
        } catch (ExceptionInInitializerError i) {
            assertEquals("You must initialize- call initialize first.", i.getMessage());
            errorThrown = true;
        }
        assertTrue(errorThrown);

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