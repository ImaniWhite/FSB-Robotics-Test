package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Brent on 9/6/2015.
 * Windows 1252
 * Edited to how I think would be cleaner on around 9/30/16 by Me (Nathan)
 */
public class TeleOp extends OpMode {
    double FRval;
    double FLval;
    double BRval;
    double BLval;

    double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
            0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };
    double dScale = 0.0;

    DcMotor FR;
    DcMotor FL;
    DcMotor BR;
    DcMotor BL;

    //Servo claw;
    //Servo arm;


    public TeleOp() {

    }

    /*
     * Code to run when the op mode is initialized goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
     */
    @Override
    public void init() {


        BR = hardwareMap.dcMotor.get("BR");
        BL = hardwareMap.dcMotor.get("BL");
        FR = hardwareMap.dcMotor.get("FR");
        FL = hardwareMap.dcMotor.get("FL");
        BR.setDirection(DcMotor.Direction.REVERSE);
        FR.setDirection(DcMotor.Direction.REVERSE);

        //arm = hardwareMap.servo.get("servo_1");
        //claw = hardwareMap.servo.get("servo_6");
    }

    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop() {

        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: left_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right

        double y1 = -gamepad1.left_stick_y;
        double x1 = gamepad1.left_stick_x; //So, is this and y2 useless?
        double y2 = -gamepad1.right_stick_y;
        double x2 = gamepad1.right_stick_x;

        y1 = scaleInput(y1);
        x2 = scaleInput(x2);

        if (Math.abs(x2) > .05) {
            FLval = -x2;
            BLval = -x2;
            FRval = x2;
            BRval = x2;
        }
        else
        if (Math.abs(y1) > .05) {

            BRval = y1;
            BLval = y1;
            FRval = y1;
            FLval = y1;
        }

        else {
            FLval = 0;
            FRval = 0;
            BRval = 0;
            BLval = 0;
        }

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        //right = (float)scaleInput(right);
        //left =  (float)scaleInput(left);

        Range.clip(FLval, -1, 1);
        Range.clip(BLval, -1, 1);
        Range.clip(BRval, -1, 1);
        Range.clip(FRval, -1, 1);

        // write the values to the motors
        FR.setPower(FRval);
        FL.setPower(FLval);
        BR.setPower(BRval);
        BL.setPower(BLval);


        if (gamepad1.a) {
            FL.setPower(0);
        }

        if (gamepad1.y) {
        }

        if (Math.abs(x2) > .05) {
            /*FLval = -x2;
            BLval = -x2;
            FRval = x2;
            BRval = x2; */
        }
        else
        if (Math.abs(y1) > .05) {

            /*BRval = y1;
            BLval = y1;
            FRval = y1;
            FLval = y1;*/
        }

        else {
            /*FLval = 0;
            FRval = 0;
            BRval = 0;
            BLval = 0;*/
        }


        telemetry.addData("Text", "*** Robot Data***");

        telemetry.addData("left tgt pwr",  "left front pwr: " + String.format("%.2f", y1));
        telemetry.addData("right tgt pwr", "right front pwr: " + String.format("%.2f", x2));

    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

    }

    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    //This method has been edited to hopefully make it better and clearer by Nathan
    double scaleInput(double dVal) {

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16);

        index = Math.abs(index);
        //Note to self: try without this
        if (index > 16) //I don't understand why this is necessary; if the input must be -1 to 1, then surely it is impossible for the index to be > 16. Kept and remade it anyway
            index = 16;

        dScale = 0.0;

        dScale = (dVal / Math.abs(dVal)) * scaleArray[index];

        return dScale;
    }
}