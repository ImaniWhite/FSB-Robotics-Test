package org.firstinspires.ftc.robotcontroller.external.samples;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Imani on 10/7/2016.
 */
public class MechTurning extends OpMode {
    double FRval;
    double FLval;
    double BRval;
    double BLval;

    DcMotor FrontRight;
    DcMotor FrontLeft;
    DcMotor BackRight;
    DcMotor BackLeft;
    //DcMotor Shooter;


    //Servo claw;
    //Servo arm;


    public MechTurning() {

    }

    /*
     * Code to run when the op mode is initialized goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#init()
     */
    @Override
    public void init() {


        BackRight = hardwareMap.dcMotor.get("BR");
        BackLeft = hardwareMap.dcMotor.get("BL");
        FrontRight = hardwareMap.dcMotor.get("FR");
        FrontLeft = hardwareMap.dcMotor.get("FL");
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
        //double y2 = -gamepad1.right_stick_y;
        //double x2 = gamepad1.right_stick_x;

        double accel = gamepad1.left_trigger; //Left is to accelerate, right is to brake
        double brake = gamepad1.right_trigger;

        double angle; //I'm assuming degrees

        //On a scale of 1, -1, if it's less than 0.05, then it may be 0 in reality. 12.75 in 255 land
        if (Math.abs(x1) > 12.75)
            x1 = 0;
        if (Math.abs(y1) > 12.75)
            y1 = 0;


        //Note: not sure when to really use the >= instead of the >. Shouldn'r matter, though
        if ((x1 >= 0) && (y1 >= 0)) //Going towards the forwards right
        {
            if (x1 == 0) //Making the angle be the rate of speed change. I put a copy of the declaration in each one 'cause I wanted a easy way to deal with /0 and 90/-90s
                angle = 90; //I'm SORRY about the redundancy, but I'm tired and whatever and this works I think
            else {
                angle = Math.atan(y1 / x1);
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

        //Making the acceleration/brake scale and then affect current speed. The + or - brake/accel depend on whether the thing is going forwards or backwards
        FRval += Range.scale(accel, 0, 255, 0, Math.abs(FRval)) * (FRval/Math.abs(FRval)) - Range.scale(brake, 0, 255, 0, Math.abs(FRval)) * (FRval/Math.abs(FRval));
        FLval += Range.scale(accel, 0, 255, 0, Math.abs(FLval)) * (FLval/Math.abs(FLval)) - Range.scale(brake, 0, 255, 0, Math.abs(FLval)) * (FLval/Math.abs(FLval));
        BRval += Range.scale(accel, 0, 255, 0, Math.abs(BRval)) * (BRval/Math.abs(BRval)) - Range.scale(brake, 0, 255, 0, Math.abs(BRval)) * (BRval/Math.abs(BRval));
        BLval += Range.scale(accel, 0, 255, 0, Math.abs(BLval)) * (BLval/Math.abs(BLval)) - Range.scale(brake, 0, 255, 0, Math.abs(BLval)) * (BLval/Math.abs(BLval));

        //Button overrides for cardinal directions. This is really sloppy and could be semi-easily changed to be nicer. Sorry
        if (gamepad1.y) //Up
            if (gamepad1.b) { //Up right
                FLval = 1;
                FRval = 0;
                BLval = 0;
                BRval = 1;
            }
            else if (gamepad1.b) { //Up left
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
        else if (gamepad1.b) //Right
            if (gamepad1.y) { //Up right
                FLval = 1;
                FRval = 0;
                BLval = 0;
                BRval = 1;
            }
            else if (gamepad1.a) { //Down right
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
        else if (gamepad1.a) //Down
            if (gamepad1.b) { //Down right
                FLval = -1;
                FRval = 0;
                BLval = 0;
                BRval = -1;
            }
            else if (gamepad1.x) { //Down left
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
        else if (gamepad1.x) //Left
            if (gamepad1.y) { //Up left
                FLval = 0;
                FRval = 1;
                BLval = 1;
                BRval = 0;
            }
            else if (gamepad1.a) { //Down left
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



        Range.clip(FLval, -1, 1); //This is to make sure that no STRANGE values somehow get in
        Range.clip(BLval, -1, 1);
        Range.clip(BRval, -1, 1);
        Range.clip(FRval, -1, 1);

        // write the values to the motors
        FrontRight.setPower(FRval);
        FrontLeft.setPower(FLval);
        BackRight.setPower(BRval);
        BackLeft.setPower(BLval);

        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("left tgt pwr",  "left front pwr: " + String.format("%.2f", y1));
        // telemetry.addData("right tgt pwr", "right front pwr: " + String.format("%.2f", x2));

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
