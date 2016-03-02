package com.qualcomm.ftcrobotcontroller.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * A controller for using a gyro sensor
 *
 * @see GyroSensor
 * @see DcMotor
 */
public class GyroPIDController {

    private GyroSensor gyro = null;

    private static final double KP = 15;
    private static final double BASE_POWER = .50;

    private double target = 0;

    private double leftPower = 0;
    private double rightPower = 0;

    private double directionalPower = 0f;

    private DcMotor.Direction direction = DcMotor.Direction.FORWARD;

    /**
     * Creates a new PID Controller with a given sensor
     *
     * @param gyro Your bot's gyro sensor.
     * @see GyroSensor
     */
    public GyroPIDController(GyroSensor gyro) {
        this.gyro = gyro;
    }

    /**
     * Calibrates the gyro sensor
     *
     * @see GyroSensor
     */
    public void calibrate() {
        this.gyro.calibrate();

        while (this.gyro.getHeading() != 0) ; //wait
    }

    /**
     * Reads from the sensor and updates the recommended gyro values.
     */
    public void update() {

        //Read the gyro values
        double current = gyro.getHeading();
        if (current > 180) {
            current = current - 360;
        }

        //Calculate the error in gyro terms
        double error = getTarget() - current;
        if (error == 0) {
            leftPower = Power.powerClamp(directionalPower);
            rightPower = Power.powerClamp(directionalPower);
            return;
        }

        //Convert the gyro values to something power-relevant
        double powerError = (error * KP) / 100;

        //When forward motor powers are positive
        if (getMotorDirection() == DcMotor.Direction.FORWARD) {
            leftPower = Power.powerClamp(directionalPower - powerError);
            rightPower = Power.powerClamp(directionalPower + powerError);
        }

        //When reverse motor powers are negative
        if (getMotorDirection() == DcMotor.Direction.REVERSE) {
            leftPower = Power.powerClamp(directionalPower + powerError);
            rightPower = Power.powerClamp(directionalPower - powerError);
        }
    }

    /**
     * Get the target direction
     *
     * @return The target direction
     * @see DcMotor
     */
    public double getTarget() {
        return target;
    }

    /**
     * Set the target direction, int degrees per second
     *
     * @see GyroSensor
     */
    public void setTarget(double target) {
        this.target = target;
    }

    /**
     * Gets the suggested left power
     *
     * @see DcMotor
     *
     * @return The left power
     */
    public double getLeftPower() {
        return leftPower;
    }

    /**
     * Gets the suggested right power
     *
     * @see DcMotor
     *
     * @return The right power
     */
    public double getRightPower() {
        return rightPower;
    }

    /**
     * Gets the direction of the PID controller
     *
     * @see com.qualcomm.robotcore.hardware.DcMotor.Direction
     *
     * @return he current direction
     */
    public DcMotor.Direction getMotorDirection() {
        return direction;
    }

    /**
     * Sets the direction of the PID controller
     * This should be the direction your robot is moving
     *
     * @see com.qualcomm.robotcore.hardware.DcMotor.Direction
     */
    public void setDirection(DcMotor.Direction value) {
        direction = value;

        //Update the power to be relative to our direction
        if (value == DcMotor.Direction.FORWARD) {
            this.directionalPower = BASE_POWER;
        } else {
            this.directionalPower = BASE_POWER * -1;
        }
    }

}
