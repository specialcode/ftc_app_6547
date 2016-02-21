package com.qualcomm.ftcrobotcontroller.opmodes;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.qualcomm.ftcrobotcontroller.Values;
import com.qualcomm.ftcrobotcontroller.hardware.HardwareManager;
import com.qualcomm.ftcrobotcontroller.hardware.Power;
import com.qualcomm.ftcrobotcontroller.hardware.nullware.NullGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;


public class GyroDrive extends  LinearOpMode
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
        gyro.calibrate();
        while(gyro.getHeading()!=0)
        {
            telemetry.addData("Wait Calibrating Gyro:", gyro.status() );
        }

        StartingGyroValue=gyro.getHeading();
        double leftPower = -.80;
        double rightPower=-.70;
        while (opModeIsActive())
        {
            waitOneFullHardwareCycle();
            telemetry.addData("Starting Loop Gyro:", StartingGyroValue );
            leftPower = Power.LeftDrivePID(gyro.getHeading(), StartingGyroValue, leftPower );
            rightPower = Power.RightDrivePID(gyro.getHeading(), StartingGyroValue, rightPower );
            telemetry.addData("leftPower", leftPower);
            telemetry.addData("rightPower", rightPower);

            left.setPower(leftPower);

            right.setPower(rightPower);

            telemetry.addData("Gyro", gyro.getHeading());
            telemetry.addData("Accelerometer X:", CurrentX);
            telemetry.addData("Accelerometer Y:", CurrentY);
            telemetry.addData("Accelerometer Z:", CurrentZ);
            waitOneFullHardwareCycle();

            loopCount++;
            telemetry.addData("Cycles", loopCount);
            waitOneFullHardwareCycle();
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


