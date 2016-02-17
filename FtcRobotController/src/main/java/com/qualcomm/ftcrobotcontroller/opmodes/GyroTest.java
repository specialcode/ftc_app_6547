package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.Values;
import com.qualcomm.ftcrobotcontroller.hardware.HardwareManager;
import com.qualcomm.ftcrobotcontroller.hardware.Power;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by tucker on 2/16/16.
 */
public class GyroTest extends OpMode {

    DcMotor left;
    DcMotor right;

    GyroSensor gyro;


    @Override
    public void init() {
        HardwareManager manager = new HardwareManager(hardwareMap);

        left = manager.getMotor(Values.LEFT_MOTOR);
        right = manager.getMotor(Values.RIGHT_MOTOR);

        gyro = hardwareMap.gyroSensor.get(Values.GYRO);
        gyro.calibrate();
    }

    @Override
    public void loop() {
        left.setPower(Power.NORMAL_SPEED + Power.pidMod(gyro.getHeading(), 0));
        right.setPower(Power.NORMAL_SPEED - Power.pidMod(gyro.getHeading(), 0));

        telemetry.addData("Gyro", gyro.getHeading());
    }
}
