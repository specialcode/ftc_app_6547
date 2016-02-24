package com.qualcomm.ftcrobotcontroller.hardware;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by STACK0V3RFL0W on 2/20/2016.
 */
public class GyroPIDController
{
    public GyroMotorDirection GetMotorDirection()
    {
        return direction;
    }

    public void SetDirection(GyroMotorDirection value)
    {
        direction = value;
        if(value == GyroMotorDirection.Forward)
        {
            this.AdustedBasePower = basePower ;
        }
        else
        {
            this.AdustedBasePower = basePower * -1;
        }

    }

    public enum GyroMotorDirection
    {
        Forward,
        Reverse
    }
    public double KP = 15;
    public double basePower = .50;
    public double Target =0;
    public double Current =0;
    public double Error =0;
    public double LeftPower =0;
    public double RightPower =0;
    public double AdustedBasePower = 0f;
    private GyroMotorDirection direction = GyroMotorDirection.Forward;

    public ModernRoboticsI2cGyro gyro = null;

    public GyroPIDController(GyroSensor gyroSensor)
    {
        gyro = ((ModernRoboticsI2cGyro)gyroSensor);
        gyro.setHeadingMode(ModernRoboticsI2cGyro.HeadingMode.HEADING_CARTESIAN); // Changing these does nothing

    }

    public void SetTargetAngle(double target)
    {
        Target = target;
    }
    public void Initialize()
    {

            this.gyro.calibrate();

            while (this.gyro.getHeading()!=0)
            {
                //wait
            }
        this.Current = this.gyro.getHeading();
    }

    public void CalculateMotorPowers()
    {
        Current = gyro.getHeading();
        if(Current > 180)
        {
            Current = Current-360;
        }

        Error = Target - Current;
        if(Error==0)
        {
            LeftPower = CleanPower(AdustedBasePower);
            RightPower = CleanPower(AdustedBasePower);
            return;
        }


        double adjustedError = (Error* KP)/100;

        //When forward motor powers are positive
        if(this.GetMotorDirection() == GyroMotorDirection.Forward)
        {
            LeftPower = CleanPower(AdustedBasePower - adjustedError);
            RightPower = CleanPower(AdustedBasePower + adjustedError);
        }

        //When reverse motor powers are negative
        if(this.GetMotorDirection() == GyroMotorDirection.Reverse)
        {
            LeftPower = CleanPower(AdustedBasePower + adjustedError);
            RightPower = CleanPower(AdustedBasePower - adjustedError);
        }
    }

    public  double CleanPower(double dirtyValue)
    {
        if(dirtyValue<=-1)
        {
            return -.80;
        }
        if(dirtyValue >=1)
        {
            return .80;
        }
        return dirtyValue;
    }


}
