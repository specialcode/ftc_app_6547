package com.qualcomm.ftcrobotcontroller.opmodes;

import android.util.Log;

import com.qualcomm.ftcrobotcontroller.Values;
import com.qualcomm.ftcrobotcontroller.hardware.HardwareManager;
import com.qualcomm.ftcrobotcontroller.hardware.MotorRunner;
import com.qualcomm.ftcrobotcontroller.hardware.Power;
import com.qualcomm.ftcrobotcontroller.units.TimeUnit;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class BotAutonRed extends LinearOpMode {

    DcMotor motorRight;
    DcMotor motorLeft;

    DcMotor tape1;

    Servo dump;

    @Override
    public void runOpMode() throws InterruptedException {
        initMotors();
        waitForStart();
        Log.w("Auton", "Starting Auton");
        //Autonomous starts here

        //Wait for motors to initialize
        Thread.sleep(1000);

        //Drive to floorgoal, backwards
        MotorRunner.run(this, new DcMotor[]{motorLeft, motorRight}, -Power.FULL_SPEED,
                new TimeUnit(Values.DRIVE_FLOORGOAL));
        //Turn to align with floor goal
        MotorRunner.run(this, motorLeft, Power.FULL_SPEED,
                new TimeUnit(Values.TURN_FLUSH));
        //Attempt to Deliver Climbers
        dump.setPosition(Values.DUMP_DOWN);
        Thread.sleep(1000);
        dump.setPosition(Values.DUMP_UP);

        //Turn to align with mountain
        motorRight.setPower(-Power.FULL_SPEED);
        MotorRunner.run(this, motorLeft, Power.FULL_SPEED,
                new TimeUnit(Values.TURN_MOUNTAIN));
        motorRight.setPower(0);

        //Drive up mountain
        MotorRunner.run(this, new DcMotor[]{motorLeft, motorRight}, Power.FULL_SPEED,
                new TimeUnit(Values.DRIVE_MOUNTAIN));

        //Try to grab a churro
        MotorRunner.run(this, tape1, Power.SLOW_SPEED,
                new TimeUnit(Values.SEND_TAPE));
        motorLeft.setPower(Power.FULL_SPEED);
        motorRight.setPower(Power.FULL_SPEED);
        for (int i = 0; i < Values.TAPE_TIMES; i++) {
            MotorRunner.run(this, tape1, -Power.NORMAL_SPEED,
                    new TimeUnit(Values.RETRACT_TAPE));
            Thread.sleep(250);
        }
        stopMotors();
    }

    public void initMotors() {
        HardwareManager manager = new HardwareManager(hardwareMap);

        motorRight = manager.getMotor(Values.RIGHT_MOTOR);
        motorLeft = manager.getMotor(Values.LEFT_MOTOR);

        tape1 = manager.getMotor(Values.TAPE_1);

        dump = manager.getServo(Values.DUMP);
    }

    public void stopMotors() {
        motorLeft.setPower(Power.FULL_STOP);
        motorRight.setPower(Power.FULL_STOP);
    }
}

