package com.qualcomm.ftcrobotcontroller.opmodes;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.ftcrobotcontroller.Values;
import com.qualcomm.ftcrobotcontroller.hardware.GyroPIDController;
import com.qualcomm.ftcrobotcontroller.hardware.HardwareManager;
import com.qualcomm.ftcrobotcontroller.hardware.nullware.NullGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by STACK0V3RFL0W on 3/5/2016.
 */

public class GyroWithAccelerometer extends LinearOpMode {

    static double currentX = 0f;
    static double CurrentY = 0f;
    static double currentZ = 0f;
    static double StartingGyroValue = 0f;
    private static SensorManager mSensorManager;
    boolean gyroFailedToLoad = false;
    private double loopCount = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();
        DcMotor left;
        DcMotor right;

        GyroSensor gyro = null;

        HardwareManager manager = new HardwareManager(hardwareMap);

        InitializeAccelerometer();

        //driving backwards things are reversed from the configuration
        right = manager.getMotor(Values.LEFT_MOTOR);
        left = manager.getMotor(Values.RIGHT_MOTOR);

        try {
            gyro = hardwareMap.gyroSensor.get(Values.GYRO);

        } catch (Exception ex) {
            //Empty Catch If the hardware map fails to find the gyro it will throw.
            //For unit testing initialize with a nullGyro
        }

        if (gyro == null) {
            gyro = new NullGyro();
            gyroFailedToLoad = true;

        }
        GyroPIDController pidController = new GyroPIDController(gyro);
        pidController.setDirection(GyroPIDController.gyroMotorDirection.Reverse);
        pidController.initialize();
        pidController.setTargetAngle(0);


        if (gyroFailedToLoad) {
            telemetry.addData("Gyro failed to load check hardware map", gyro.toString());

        }

        while (opModeIsActive()) {
            waitOneFullHardwareCycle();


            telemetry.addData("Starting Gyro:", pidController.getTarget());
            telemetry.addData("Starting Gyro:", pidController.gyro.getDeviceName());

            pidController.calculateMotorPowers();

            telemetry.addData("leftPower", pidController.getLeftPower());
            telemetry.addData("rightPower", pidController.getRightPower());
            telemetry.addData("GyroHeading:", pidController.getCurrent());

            left.setPower(pidController.getLeftPower());
            right.setPower(pidController.getRightPower());

            telemetry.addData("Gyro", gyro.getHeading());
            telemetry.addData("Gyro", gyro.getHeading());
            telemetry.addData("Accelerometer X:", currentX);
            telemetry.addData("Accelerometer Y:", CurrentY);
            telemetry.addData("Accelerometer Z:", currentZ);

            loopCount++;
            telemetry.addData("Cycles", loopCount);
        }

    }

    private void InitializeAccelerometer() {

        Sensor accelerometer;
        mSensorManager = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            SensorEventListener magnetListener = new SensorEventListener() {
                public void onAccuracyChanged(Sensor sensor, int accuracy) {
                    // do things if you're interested in accuracy changes
                }

                public void onSensorChanged(SensorEvent event) {
                    currentX = event.values[0];
                    CurrentY = event.values[1];
                    currentZ = event.values[2];
                }
            };

            mSensorManager.registerListener(magnetListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

}

