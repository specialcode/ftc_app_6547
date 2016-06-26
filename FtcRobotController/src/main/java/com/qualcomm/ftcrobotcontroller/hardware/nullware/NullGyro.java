package com.qualcomm.ftcrobotcontroller.hardware.nullware;

import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by STACK0V3RFL0W on 2/17/2016.
 */
public class NullGyro implements GyroSensor
{
    int _heading = 0;

    @Override
    public void calibrate() {

    }

    @Override
    public boolean isCalibrating() {
        return false;
    }

    @Override
    public int getHeading() {
        return _heading;
    }

    public void setHeading(int value) {
        _heading = value;
    }
    @Override
    public double getRotation() {
        return 0;
    }

    @Override
    public int rawX() {
        return 0;
    }

    @Override
    public int rawY() {
        return 0;
    }

    @Override
    public int rawZ() {
        return 0;
    }

    @Override
    public void resetZAxisIntegrator() {

    }

    @Override
    public String status() {
        return null;
    }

    @Override
    public String getDeviceName() {
        return null;
    }

    @Override
    public String getConnectionInfo() {
        return null;
    }

    @Override
    public int getVersion() {
        return 0;
    }

    @Override
    public void close() {

    }
}
