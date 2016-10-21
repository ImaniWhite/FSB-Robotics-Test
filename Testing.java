package org.firstinspires.ftc.robotcontroller.external.samples;

import android.hardware.Sensor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Testing extends OpMode {
    // Changeable motor values
    double FRight;
    double FLeft;
    double BRight;
    double BLeft;

    double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
            0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };
    double dScale = 0.0;

    // Motors
    DcMotor FR;
    DcMotor FL;
    DcMotor BR;
    DcMotor BL;

    //Servos n' stuff
    Servo swepper;
    Sensor something;


    @Override
    public void init() {

        BR = hardwareMap.dcMotor.get("BR");
        BL = hardwareMap.dcMotor.get("BL");
        FR = hardwareMap.dcMotor.get("FR");
        FL = hardwareMap.dcMotor.get("FL");
        BR.setDirection(DcMotor.Direction.REVERSE);
        FR.setDirection(DcMotor.Direction.REVERSE);



    }

    @Override
    public void loop() {

        double y1 = -gamepad1.left_stick_y;
        double x1 = gamepad1.left_stick_x;
        double y2 = -gamepad1.right_stick_y;
        double x2 = gamepad1.right_stick_x;

        scaleInput(y1);
        scaleInput(x2);

        if (Math.abs(x2) > .05) {
            FLeft = -x2;
            BLeft = -x2;
            FRight = x2;
            BRight = x2;
        } else if (Math.abs(y1) > .05) {

            BRight = y1;
            BLeft = y1;
            FRight = y1;
            FLeft = y1;
        } else {
            FLeft = 0;
            BRight = 0;
            FRight = 0;
            BLeft = 0;

        }

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        //right = (float)scaleInput(right);
        //left =  (float)scaleInput(left);

        Range.clip(FLeft, -1, 1);
        Range.clip(BLeft, -1, 1);
        Range.clip(BRight, -1, 1);
        Range.clip(FRight, -1, 1);

        // write the values to the motors
        FR.setPower(FRight);
        FL.setPower(FLeft);
        BR.setPower(BRight);
        BL.setPower(BLeft);




        telemetry.addData("Text", "*** Robot Data***");

        telemetry.addData("left tgt pwr", "left front pwr: " + String.format("%.2f", y1));
        telemetry.addData("right tgt pwr", "right front pwr: " + String.format("%.2f", x2));

    }

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
