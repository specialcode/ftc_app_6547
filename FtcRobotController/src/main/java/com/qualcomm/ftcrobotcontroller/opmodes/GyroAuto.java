package com.qualcomm.ftcrobotcontroller.opmodes;

import android.hardware.SensorManager;

import com.qualcomm.ftcrobotcontroller.Values;
import com.qualcomm.ftcrobotcontroller.hardware.GyroPIDController;
import com.qualcomm.ftcrobotcontroller.hardware.HardwareManager;
import com.qualcomm.ftcrobotcontroller.hardware.nullware.NullGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;


public class GyroAuto extends  LinearOpMode
{

    public static double CurrentX = 0f;
    public static double CurrentY = 0f;
    public static double CurrentZ = 0f;
    public static double StartingGyroValue=0f;
    private static SensorManager mSensorManager;
    private double loopCount = 0;

    @Override
    public void runOpMode() throws InterruptedException
    {
        waitForStart();
        DcMotor left;
        DcMotor right;

        GyroSensor gyro= null;

        HardwareManager manager = new HardwareManager(hardwareMap);

        //driving backwards things are reversed from the configuration
        right = manager.getMotor(Values.LEFT_MOTOR);
        left = manager.getMotor(Values.RIGHT_MOTOR);

        try
        {
            gyro = hardwareMap.gyroSensor.get(Values.GYRO);
            //((ModernRoboticsI2cGyro)gyro).setHeadingMode(ModernRoboticsI2cGyro.HeadingMode.HEADING_CARTESIAN);
        }
        catch(Exception ex)
        {}

        if(gyro==null)
        {
            gyro = new NullGyro();
        }
        GyroPIDController pidController = new GyroPIDController(gyro);
        //  pidController.setDirection(DcMotor.Direction.REVERSE);
        //  pidController.calibrate();
        //   pidController.setTarget(0);


        while (opModeIsActive())
        {
            waitOneFullHardwareCycle();
            //     telemetry.addData("Starting Gyro:", pidController.getTarget());
            //     telemetry.addData("Starting Gyro:", gyro.getDeviceName());
            //     pidController.update();
            //      telemetry.addData("leftPower", pidController.getLeftPower());
            //    telemetry.addData("rightPower", pidController.getRightPower());
            //      telemetry.addData("GyroHeading:", gyro.getHeading());
            //      left.setPower(pidController.getLeftPower());
            //     right.setPower(pidController.getRightPower());
            telemetry.addData("Gyro", gyro.getHeading());
            telemetry.addData("Gyro", gyro.getHeading());
            telemetry.addData("Accelerometer X:", CurrentX);
            telemetry.addData("Accelerometer Y:", CurrentY);
            telemetry.addData("Accelerometer Z:", CurrentZ);
            waitOneFullHardwareCycle();
            loopCount++;
            telemetry.addData("Cycles", loopCount);

        }

    }

}


