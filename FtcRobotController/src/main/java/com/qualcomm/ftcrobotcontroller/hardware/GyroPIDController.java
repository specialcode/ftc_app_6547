package com.qualcomm.ftcrobotcontroller.hardware;

import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by STACK0V3RFL0W on 2/20/2016.
 */
public class GyroPIDController {
    public GyroSensor gyro = null;
    double KP = 15;
    double basePower = .50;
    double target = 0;
    double error = 0;
    double leftPower = 0;
    double rightPower = 0;
    double adjustedBasePower = 0f;
    private double current = 0;
    private gyroMotorDirection direction = gyroMotorDirection.Forward;

    public GyroPIDController(GyroSensor gyroSensor) {
        gyro = gyroSensor;
        //gyro.setHeadingMode(ModernRoboticsI2cGyro.HeadingMode.HEADING_CARTESIAN); // Changing these does nothing
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
        this.setCurrent(this.gyro.getHeading());
    }

    public void calculateMotorPowers() {
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

    public enum gyroMotorDirection {
        Forward,
        Reverse
    }


}
