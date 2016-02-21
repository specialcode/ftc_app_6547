package com.qualcomm.ftcrobotcontroller.hardware;

/**
 * Functions and constants for working with power values.
 */
public class Power {

    public static final double NORMAL_SPEED = 0.75;
    public static final double FULL_SPEED = 1.0;
    public static final double SLOW_TURN = 0.15;
    public static final double FULL_STOP = 0.0;
    public static final double SLOW_SPEED = 0.4;

    public static final double KP = 1.25;

    /**
     * Curves a value with a cubic eqaution
     * Intended to be used with controller input
     *
     * @param x The value to clamp.
     * @return The clamped value.
     */
    public static double speedCurve(double x) {
        return (0.598958 * Math.pow(x, 3)) - (4.43184 * Math.pow(10, -16) * Math.pow(x, 2)) + (0.201042 * x);
    }

    /**
     * Clamps a value between 0 and 1
     *
     * @param x The value to clamp.
     * @return The clamped value.
     */
    public static double powerClamp(double x) {
        if (x > 1) {
            return 1;
        }
        if (x < 0) {
            return 0;
        }
        return x;
    }

    public static double pidMod(int gyro, int target)
    {
        int error = gyro - target;
        if(error == 0)
        {
            return Power.NORMAL_SPEED;
        }
        else
        {
            return KP * error/100;
        }
    }

    public static double LeftDrivePID(double gyro, double target, double power)
    {
        //Motors are running reverse
        double error=0f;
        double returnValue;
        if(gyro <= 180)
        {
            error = gyro+ target;
        }
       if(gyro <=360 && gyro >180)
        {
            error = target -Math.abs(gyro-360);
            if(error==0)
            {
                error =-1;
            }
        }
        if(error == 0)
        {
            return -.80;
        }

        if(error < 0) // We are turning left - give more power to left and less to right
        {
            returnValue =  power - Math.abs((KP * error)/100);
        }
        else
        {
            returnValue= power +  Math.abs((KP* error)/100);
        }

        return CleanPower(returnValue);
    }

    public static double RightDrivePID(double gyro, double target, double power)
    {
        double error=0f;
        double returnValue;
        if(gyro <= 180 )
        {
            error = gyro+ target;
        }
        if(gyro <=360 && gyro >180)
        {
            error = target -Math.abs(gyro-359);
            if(error==0)
            {
                error =-1;
            }
        }

        if(error == 0)
        {
            return -.70;
        }


        if(error > 0) // We are turning right - give more power to right and less to left
        {
            returnValue =  power - Math.abs((KP * error)/100);

        }
        else
        {
            returnValue= power +  Math.abs((KP* error)/100);
        }

        return CleanPower(returnValue);
    }

    //Ensure powerrange is between -1 and 1
public static double CleanPower(double dirtyValue)
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
