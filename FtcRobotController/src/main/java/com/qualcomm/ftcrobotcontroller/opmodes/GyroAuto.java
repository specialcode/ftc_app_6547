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


public class GyroAuto extends  LinearOpMode
{

    private double loopCount=0;
    public static double CurrentX = 0f;
    public static double CurrentY = 0f;
    public static double CurrentZ = 0f;
    public static double StartingGyroValue=0f;
    private static SensorManager mSensorManager;

    @Override
    public void runOpMode() throws InterruptedException
    {
        waitForStart();
        DcMotor left;
        DcMotor right;

        GyroSensor gyro= null;

        HardwareManager manager = new HardwareManager(hardwareMap);

        InititalizeAccelerometer();

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
        pidController.SetDirection(GyroPIDController.GyroMotorDirection.Reverse);
        pidController.Initialize();
        pidController.SetTargetAngle(0);


        while (opModeIsActive())
        {
            waitOneFullHardwareCycle();
            telemetry.addData("Starting Gyro:", pidController.Target );
            pidController.CalculateMotorPowers();
            telemetry.addData("leftPower", pidController.LeftPower);
            telemetry.addData("rightPower", pidController.RightPower);
            telemetry.addData("GyroHeading:", pidController.Current );
            left.setPower(pidController.LeftPower);

            right.setPower(pidController.RightPower);

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

    private void InititalizeAccelerometer()
    {

        Sensor accelerometer;
        mSensorManager = (SensorManager) hardwareMap.appContext.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null)
        {
            SensorEventListener magnetListener = new SensorEventListener()
            {
                public void onAccuracyChanged(Sensor sensor, int accuracy)
                {
                    // do things if you're interested in accuracy changes
                }
                public void onSensorChanged(SensorEvent event)
                {
                    GyroDrive.CurrentX = event.values[0];
                    GyroDrive.CurrentY = event.values[1];
                    GyroDrive.CurrentZ = event.values[2];
                }
            };

            mSensorManager.registerListener(magnetListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

}


