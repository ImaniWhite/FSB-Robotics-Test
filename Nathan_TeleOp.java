
package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Imani and Nathan on 10/5/2016.
 */
public class Nathan_TeleOp extends OpMode {

    double FRval;
    double FLval;
    double BRval;
    double BLval;

    //LIST OF PROBLEMOS
    //1. ACCEL DOESN'T WORK, ACTS AS BRAKE FOR BACKWARDS WHY'
    //2. ENTIRE CIRCLE SHIFTED AND CHANGES RAPIDLY

    DcMotor FrontRight;
    DcMotor FrontLeft;
    DcMotor BackRight;
    DcMotor BackLeft;
    //DcMotor Shooter;

    final int MAX = 1; //Imani can't remember what the max is, and we can't test the damn robot. SO, I'm giving the max a constant for quick edits later

    //Servo claw;
    ///Servo arm;


    public Nathan_TeleOp() {

    }

    /*
     * Code to run when the op mode is initialized goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
     */
    @Override
    public void init() {


        BackRight = hardwareMap.dcMotor.get("BackRight");
        BackLeft = hardwareMap.dcMotor.get("BackLeft");
        FrontRight = hardwareMap.dcMotor.get("FrontRight");
        FrontLeft = hardwareMap.dcMotor.get("FrontLeft");
        BackRight.setDirection(DcMotor.Direction.REVERSE);
        FrontRight.setDirection(DcMotor.Direction.REVERSE);

        //Shooter = hardwareMap.dcMotor.get("shoot");
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



        double y1 = gamepad1.left_stick_y;
        double x1 = gamepad1.left_stick_x; //I don't like these being declared here, but whatever
        //double y2 = -gamepad1.right_stick_y; //For turning
        double x2 = gamepad1.right_stick_x; //This one is. Not that one

        double accel = gamepad1.left_trigger; //Left is to accelerate, right is to brake
        double brake = gamepad1.right_trigger;

        boolean xIsOn = gamepad1.x;
        boolean yIsOn = gamepad1.y;
        boolean aIsOn = gamepad1.a;
        boolean bIsOn = gamepad1.b;

        double angle; //I'm assuming degrees

        //On a scale of 1, -1, if it's less than 0.05, then it may be 0 in reality. 12.75 in 255 land
        if (Math.abs(x1) < 0.05*MAX)
            x1 = 0;
        if (Math.abs(y1) < 0.05*MAX)
            y1 = 0;
        if (Math.abs(x2) < 0.05*MAX)
            x2 = 0;
        //if (Math.abs(y2) > 0.05*MAX)
            //y2 = 0;


        //Note: not sure when to really use the >= instead of the >. Shouldn't matter, though
        if ((x1 == 0) && (y1 == 0))
        {
            FLval = 0;
            FRval = 0;
            BLval = 0;
            BRval = 0;
        }
        else
            if ((x1 >= 0) && (y1 >= 0)) //Going towards the forwards right
            {
                if (x1 == 0) //Making the angle be the rate of speed change. I put a copy of the declaration in each one 'cause I wanted a easy way to deal with /0 and 90/-90s
                    angle = 90; //I'm SORRY about the redundancy, but I'm tired and whatever and this works I think
                else {
                    angle = Math.atan(y1 / x1);
                    if (((y1/x1) > 0.95) && ((y1/x1) < 1.05))
                        angle = 1;
                    angle = (360 * angle)/(2 * Math.PI); //Converting from radians to degrees
                }

                FLval = 1;
                BRval = 1;

                //To get the angle value to work with a forwards and backwards whatevah
                FRval = (angle - 45)/45;
                BLval = (angle - 45)/45;
            }
            else if ((x1 >= 0) && (y1 <= 0)) //Going towards backwards right
            {
                if (x1 == 0)
                    angle = -90;
                else {
                    angle = Math.atan(y1 / x1);
                    if (((y1/x1) > 0.95) && ((y1/x1) < 1.05))
                        angle = 1;
                    angle = (360 * angle)/(2 * Math.PI); //Converting from radians to degrees
                }

                FRval = -1;
                BLval = -1;

                FLval = (angle + 45)/45;
                BRval = (angle + 45)/45;
            }
            else if ((x1 <= 0) && (y1 <= 0)) //Backwards left
            {
                if (x1 == 0)
                    angle = 90;
                else {
                    angle = Math.atan(y1/x1);
                    if (((y1/x1) > 0.95) && ((y1/x1) < 1.05))
                        angle = 1;
                    angle = (360 * angle)/(2 * Math.PI); //Converting from radians to degrees
                }

                FLval = -1;
                BRval = -1;

                FRval = (-angle + 45)/45;
                BLval = (-angle + 45)/45;
            }
            else if ((x1 <= 0) && (y1 >=0)) //Forwards left
            {
                if (x1 == 0)
                    angle = -90;
                else {
                    angle = Math.atan(y1/x1);
                    if (((y1/x1) > 0.95) && ((y1/x1) < 1.05))
                        angle = 1;
                    angle = (360 * angle)/(2 * Math.PI); //Converting from radians to degrees
                }

                FRval = 1;
                BLval = 1;

                FLval = (-angle - 45)/45;
                BRval = (-angle - 45)/45;
            }

        //Setting up so default half speed
        FRval /= 2;
        FLval /= 2;
        BRval /= 2;
        BLval /= 2;

        /**
        //Making the acceleration/brake scale and then affect current speed. The + or - brake/accel depend on whether the thing is going forwards or backwards
        FRval += Range.scale(accel, 0, MAX, 0, Math.abs(FRval)) * (FRval/Math.abs(FRval)) - Range.scale(brake, 0, MAX, 0, Math.abs(FRval)) * (FRval/Math.abs(FRval));
        FLval += Range.scale(accel, 0, MAX, 0, Math.abs(FLval)) * (FLval/Math.abs(FLval)) - Range.scale(brake, 0, MAX, 0, Math.abs(FLval)) * (FLval/Math.abs(FLval));
        BRval += Range.scale(accel, 0, MAX, 0, Math.abs(BRval)) * (BRval/Math.abs(BRval)) - Range.scale(brake, 0, MAX, 0, Math.abs(BRval)) * (BRval/Math.abs(BRval));
        BLval += Range.scale(accel, 0, MAX, 0, Math.abs(BLval)) * (BLval/Math.abs(BLval)) - Range.scale(brake, 0, MAX, 0, Math.abs(BLval)) * (BLval/Math.abs(BLval));
        */

        //BEHOLD, CITIZENS, IT IS I, THE SLOPPY CODE SUPERHERO! IS THAT THING YOU MADE JUST UP THERE NOT WORKING?
        //COMPLETELY COMMENT IT OUT, THEN WRITE SOMETHING WITH TONS OF SUPERFLUOUS VARIABLES AND CONDITIONALS!
        //I MUST LEAVE. MY PEOPLE NEED ME. *WHOOOOOOOOOOOOOOOOOOOOOOOOSSSSSSSSSSSSSSSSHHHHHHHHHHHHHHHHH*

        boolean isNegativeFLval = (FLval < 0), isNegativeFRval = (FRval < 0), isNegativeBLval = (BLval < 0), isNegativeBRval = (BRval < 0);

        if (isNegativeFRval)
            FRval -= (accel/MAX)*Math.abs(FRval) - (brake/MAX)*Math.abs(FRval);
        else
            FRval += (accel/MAX)*Math.abs(FRval) - (brake/MAX)*Math.abs(FRval);
        if (isNegativeFLval)
            FLval -= (accel/MAX)*Math.abs(FLval) - (brake/MAX)*Math.abs(FLval);
        else
            FLval += (accel/MAX)*Math.abs(FLval) - (brake/MAX)*Math.abs(FLval);
        if (isNegativeBRval)
            BRval -= (accel/MAX)*Math.abs(BRval) - (brake/MAX)*Math.abs(BRval);
        else
            BRval += (accel/MAX)*Math.abs(BRval) - (brake/MAX)*Math.abs(BRval);
        if (isNegativeBLval)
            BLval -= (accel/MAX)*Math.abs(BLval) - (brake/MAX)*Math.abs(BLval);
        else
            BLval += (accel/MAX)*Math.abs(BLval) - (brake/MAX)*Math.abs(BLval);

        //Button overrides for cardinal directions. This is really sloppy and could be semi-easily changed to be nicer. Sorry
        if (yIsOn) //Up
            if (bIsOn) { //Up right
                FLval = 1;
                FRval = 0;
                BLval = 0;
                BRval = 1;
            }
            else if (xIsOn) { //Up left
                FLval = 0;
                FRval = 1;
                BLval = 1;
                BRval = 0;
            }
            else { //Up
                FLval = 1;
                FRval = 1;
                BLval = 1;
                BRval = 1;
            }
        else if (bIsOn) //Right
            if (yIsOn) { //Up right
                FLval = 1;
                FRval = 0;
                BLval = 0;
                BRval = 1;
            }
            else if (aIsOn) { //Down right
                FLval = -1;
                FRval = 0;
                BLval = 0;
                BRval = -1;
            }
            else { //Right
                FLval = 1;
                FRval = -1;
                BLval = -1;
                BRval = 1;
            }
        else if (aIsOn) //Down
            if (bIsOn) { //Down right
                FLval = -1;
                FRval = 0;
                BLval = 0;
                BRval = -1;
            }
            else if (xIsOn) { //Down left
                FLval = 0;
                FRval = -1;
                BLval = -1;
                BRval = 0;
            }
            else { //Down
                FLval = -1;
                FRval = -1;
                BLval = -1;
                BRval = -1;
            }
        else if (xIsOn) //Left
            if (yIsOn) { //Up left
                FLval = 0;
                FRval = 1;
                BLval = 1;
                BRval = 0;
            }
            else if (aIsOn) { //Down left
                FLval = -1;
                FRval = 0;
                BLval = 0;
                BRval = -1;
            }
            else { //Left
                FLval = -1;
                FRval = 1;
                BLval = 1;
                BRval = -1;
            }

        //Right now turning overrides other movement. Wouldn't it be nice if we could TEST it, though?
        if (x2 != 0) //Turning, I hope?
        {
            //Range.scale(x2, -MAX, MAX, -1, 1); //This is unnecessary if, like I expect, the range is already -1 to 1

            FRval = -x2/MAX;
            FLval = x2/MAX;
            BRval = -x2/MAX;
            BLval = x2/MAX;
        }

        Range.clip(FLval, -0.9, 0.9); //This is to make sure that no STRANGE values somehow get in
        Range.clip(BLval, -0.9, 0.9);
        Range.clip(BRval, -0.9, 0.9);
        Range.clip(FRval, -0.9, 0.9);

        /**
        FRval = 1;
        FLval = -1;
        BRval = 1;
        BLval = -1;
        */

        // write the values to the motors
        FrontRight.setPower(FRval);
        FrontLeft.setPower(FLval);
        BackRight.setPower(BRval);
        BackLeft.setPower(BLval);

        telemetry.addData("Text", "*** Robot Data ***");
        //telemetry.addData("left tgt pwr",  "left front pwr: " + String.format("%.2f", y1));
        //telemetry.addData("right tgt pwr", "right front pwr: " + String.format("%.2f", x1));
    }

    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */
    @Override
    public void stop() {

    }

}