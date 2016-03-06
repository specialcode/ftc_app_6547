package com.qualcomm.ftcrobotcontroller.hardware;

import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by STACK0V3RFL0W on 2/20/2016.
 *
 * For an example of class usage see the unit tests
 *  ---    testUsageExample()  in GyroPIDControllerTest in androidTest folder
 *
 *
 */
public class GyroPIDController {
    public GyroSensor gyro = null;
    double KP = 15;
    double basePower = .50;
    double target = 0;
    double error = 0;
    double leftPower = 0;
    double rightPower = 0;

    // use to store the power as a percentage
    double adjustedBasePower = 0f;
    double current = 0;
    boolean isInitialized = false;

    private gyroMotorDirection direction = gyroMotorDirection.Forward;

    public GyroPIDController(GyroSensor gyroSensor) {
        gyro = gyroSensor;

    }

    public gyroMotorDirection getMotorDirection() {
        return direction;
    }

    public void setDirection(gyroMotorDirection value) {
        direction = value;
        if (value == gyroMotorDirection.Forward) {
            this.adjustedBasePower = basePower;
        } else {
            this.adjustedBasePower = basePower * -1;
        }

    }

    public double getTarget() {
        return target;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public double getLeftPower() {
        return leftPower;
    }

    public void setLeftPower(double leftPower) {
        this.leftPower = leftPower;
    }

    public double getRightPower() {
        return rightPower;
    }

    public void setRightPower(double rightPower) {
        this.rightPower = rightPower;
    }

    public boolean getIsInitialized() {
        return this.isInitialized;
    }

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

    public void setTargetAngle(double target) {
        this.setTarget(target);
    }

    public void initialize() {
        this.gyro.calibrate();

        while (this.gyro.getHeading() != 0) {
            //wait
        }
        this.isInitialized = true;
        this.setCurrent(this.gyro.getHeading());
    }

    public void calculateMotorPowers() {
        if (!this.isInitialized) {
            throw new ExceptionInInitializerError("You must initialize- call initialize first.");
        }
        setCurrent(gyro.getHeading());
        if (getCurrent() > 180) {
            setCurrent(getCurrent() - 360);
        }

        error = getTarget() - getCurrent();
        if (error == 0) {
            setLeftPower(cleanPower(adjustedBasePower));
            setRightPower(cleanPower(adjustedBasePower));
            return;
        }

// the motors run below 1 so we are going to divide by 100 to get it to a decimal range
        double adjustedError = (error * KP) / 100;

        //When forward motor powers are positive
        if (this.getMotorDirection() == gyroMotorDirection.Forward) {
            setLeftPower(cleanPower(adjustedBasePower - adjustedError));
            setRightPower(cleanPower(adjustedBasePower + adjustedError));
        }

        //When reverse motor powers are negative
        if (this.getMotorDirection() == gyroMotorDirection.Reverse) {
            setLeftPower(cleanPower(adjustedBasePower + adjustedError));
            setRightPower(cleanPower(adjustedBasePower - adjustedError));
        }
    }

    public double cleanPower(double dirtyValue) {
        if (dirtyValue <= -1) {
            return -.80;
        }
        if (dirtyValue >= 1) {
            return .80;
        }
        return dirtyValue;
    }

    // A new enum was created here as opposed to the one in the motors class so GyroPidController will not have an unneeded dependency
    public enum gyroMotorDirection {
        Forward,
        Reverse
    }


}
