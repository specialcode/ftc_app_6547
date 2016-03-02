package com.qualcomm.ftcrobotcontroller.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by STACK0V3RFL0W on 2/20/2016.
 */
public class GyroPIDController {

    private GyroSensor gyro = null;

    private static double KP = 15;
    private static double basePower = .50;

    private double target = 0;

    private double leftPower = 0;
    private double rightPower = 0;

    private double adustedBasePower = 0f;

    private DcMotor.Direction direction = DcMotor.Direction.FORWARD;

    public GyroPIDController(GyroSensor gyroSensor) {
        gyro = gyroSensor;
    }

    public void calibrate() {
        this.gyro.calibrate();

        while (this.gyro.getHeading() != 0) {
            //wait
        }
    }

    public void update() {
        double current = gyro.getHeading();
        if (current > 180) {
            current = current - 360;
        }

        double error = getTarget() - current;
        if (error == 0) {
            setLeftPower(Power.powerClamp(adustedBasePower));
            setRightPower(Power.powerClamp(adustedBasePower));
            return;
        }


        double adjustedError = (error * KP) / 100;

        //When forward motor powers are positive
        if (this.getMotorDirection() == DcMotor.Direction.FORWARD) {
            setLeftPower(Power.powerClamp(adustedBasePower - adjustedError));
            setRightPower(Power.powerClamp(adustedBasePower + adjustedError));
        }

        //When reverse motor powers are negative
        if (this.getMotorDirection() == DcMotor.Direction.REVERSE) {
            setLeftPower(Power.powerClamp(adustedBasePower + adjustedError));
            setRightPower(Power.powerClamp(adustedBasePower - adjustedError));
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

    public DcMotor.Direction getMotorDirection() {
        return direction;
    }

    public void setDirection(DcMotor.Direction value) {
        direction = value;
        if (value == DcMotor.Direction.FORWARD) {
            this.adustedBasePower = basePower;
        } else {
            this.adustedBasePower = basePower * -1;
        }
    }

}
