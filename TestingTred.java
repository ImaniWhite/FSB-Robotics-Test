package org.firstinspires.ftc.robotcontroller.external.samples;

import android.hardware.Sensor;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class TestingTred extends OpMode {
    // Changeable motor values
    double Right;
    double Left;


    // Motors
    DcMotor FR;//right motor
    DcMotor FL;//left motor

    double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
            0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };
    double dScale = 0.0;

    //Servos n' stuff
    Servo swepper; //smaller more direction based motors rather than run time based
    Sensor something; //collect data and sends it back to computer AKA the phone


    @Override
    public void init() {


        FR = hardwareMap.dcMotor.get("FR");
        FL = hardwareMap.dcMotor.get("FL");
        FL.setDirection((DcMotor.Direction.REVERSE));
        FR.setDirection(DcMotor.Direction.REVERSE);



    }

    @Override
    public void loop() {
        //defining the gamepad sticks
        double y1 = -gamepad1.left_stick_y;
        double y2 = -gamepad1.right_stick_y;
        y1 = scaleInput(y1);
        y2 = scaleInput(y2);

        Right = 0;
        Left = 0;

        if(Math.abs(y2) > 0.05)
            Right = y2;
        if(Math.abs(y1) > 0.05)
            Left = y1;



        /* Old stuff that doesn't work well?
        if (Math.abs(y2) > .05) {
            Left = -y2;
            Right = y2;
        } else if (Math.abs(y1) > .05) {

            Right = y1;
            Left = y1;
        } else {
            Left = 0;
            Right = 0;
        }
        */

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        //right = (float)scaleInput(right);
        //left =  (float)scaleInput(left);

        //Mores stuff added by Nathan
        if ((gamepad1.left_bumper && Left != 0) && Right != 0)  {
            Left = (Left / Math.abs(Left));
            Right = (Right / Math.abs(Right));
        }
        if (gamepad1.right_bumper) {
            Left = 0;
            Right = 0;
        }

        Range.clip(Left, -1, 1);
        Range.clip(Right, -1, 1);

        // write the values to the motors
        FR.setPower(Right);
        FL.setPower(Left);


        telemetry.addData("Text", "*** Robot Data***");

        telemetry.addData("left tgt pwr", "left front pwr: " + String.format("%.2f", y1));
        telemetry.addData("right tgt pwr", "right front pwr: " + String.format("%.2f", y2));

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
