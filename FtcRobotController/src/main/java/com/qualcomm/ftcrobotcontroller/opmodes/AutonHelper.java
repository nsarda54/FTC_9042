package com.qualcomm.ftcrobotcontroller.opmodes;

        import android.hardware.Sensor;

        import com.qualcomm.robotcore.eventloop.opmode.OpMode;
        import com.qualcomm.robotcore.hardware.ColorSensor;
        import com.qualcomm.robotcore.hardware.DcMotor;
        import com.qualcomm.robotcore.hardware.DcMotorController;
        import com.qualcomm.robotcore.hardware.Servo;
        import com.qualcomm.robotcore.hardware.TouchSensor;
        import com.qualcomm.robotcore.util.Range;

public class AutonHelper extends OpMode {

    //driving motors
    DcMotor frontLeft,
            backLeft;

    DcMotor frontRight,
            backRight;

    //arm motors
    DcMotor armMotor1,
            armMotor2,
            armPivot,
            propeller;

    //zipline servo
    Servo zipLiner,
          dropClimber;

    TouchSensor backBumper;

    //encoder targets
    private int rightTarget,
            leftTarget;

    //SERVO CONSTANTS
    private final double SERVO_MAX = .6,
            SERVO_MIN = .2,
            SERVO_NEUTRAL = 9.0 / 17;

    private final int PROPELLER_RIGHT = -120,
            PROPELLER_LEFT = 120;
    //Stops the continuous servo

    //MOTOR RANGES
    private final double MOTOR_MAX = 1,
            MOTOR_MIN = -1;

    protected boolean on = false;

    //ENCODER CONSTANTS
    private final double CIRCUMFERENCE_INCHES = 4 * Math.PI,
            TICKS_PER_ROTATION = 1200 / 1.05,
            TICKS_PER_INCH = TICKS_PER_ROTATION / CIRCUMFERENCE_INCHES,
            TOLERANCE = 40,
            ROBOT_WIDTH = 14.5;

    protected int targetPos,
                propellerTargetPos = PROPELLER_RIGHT;


    public AutonHelper() {

    }


    public void init() {
        //left drive
        frontLeft = hardwareMap.dcMotor.get("l1");
        backLeft = hardwareMap.dcMotor.get("l2");

        //right drive
        frontRight = hardwareMap.dcMotor.get("r1");
        backRight = hardwareMap.dcMotor.get("r2");

        //pivot motor
        armPivot = hardwareMap.dcMotor.get("arm");

        //tape measure arms
        armMotor1 = hardwareMap.dcMotor.get("tm1");
        armMotor2 = hardwareMap.dcMotor.get("tm2");
        propeller = hardwareMap.dcMotor.get("prop");

        //zipline servo
        zipLiner = hardwareMap.servo.get("zip");
        dropClimber = hardwareMap.servo.get("drop");

        backBumper = hardwareMap.touchSensor.get("bumper");



        setDirection();
        resetEncoders();
    }


    public void setDirection() {
        if (frontLeft.getDirection() == DcMotor.Direction.REVERSE) {
            frontLeft.setDirection(DcMotor.Direction.FORWARD);
        }

        if (backLeft.getDirection() == DcMotor.Direction.REVERSE) {
            backLeft.setDirection(DcMotor.Direction.FORWARD);
        }

        if (frontRight.getDirection() == DcMotor.Direction.FORWARD) {
            frontRight.setDirection(DcMotor.Direction.REVERSE);
        }

        if (backRight.getDirection() == DcMotor.Direction.FORWARD) {
            backRight.setDirection(DcMotor.Direction.REVERSE);
        }

        if (armMotor1.getDirection() == DcMotor.Direction.FORWARD) {
            armMotor1.setDirection(DcMotor.Direction.REVERSE);
        }
    }


    //ENCODER MANIPULATION
    public boolean resetEncoders() {
        frontLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        backLeft.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        frontRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);
        backRight.setMode(DcMotorController.RunMode.RESET_ENCODERS);

        return ((frontLeft.getCurrentPosition() == 0) &&
                (backLeft.getCurrentPosition() == 0) &&
                (frontRight.getCurrentPosition() == 0) &&
                (backRight.getCurrentPosition() == 0));

    }

    public void propellerSetToEncoderMode(){
        propeller.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }

    public void setToEncoderMode() {

        frontLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        backLeft.setMode(DcMotorController.RunMode.RUN_TO_POSITION);

        frontRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
    }

    public void setToWOEncoderMode() {

        frontLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        backLeft.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);

        frontRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
        backRight.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
    }


    public boolean runUntilBumped(){
        setMotorPower(.3, .3);
        if (backBumper.isPressed()){
            setMotorPower(0,0);
            setToEncoderMode();
            return true;
        }
        return false;
    }


    //ENCODER BASED MOVEMENT
    public boolean runStraight(double distance_in_inches, boolean speed) {
        leftTarget = (int) (distance_in_inches * TICKS_PER_INCH);
        rightTarget = leftTarget;
        setTargetValueMotor();

        if (speed) {
            setMotorPower(.9, .9);
        } else {
            setMotorPower(.2, .2);
        }

        if (hasReached()) {
            setMotorPower(0, 0);
            return true;
        }
        return false;
    }

    public boolean setTargetValueTurn(double degrees) {
        int encoderTarget = (int) (degrees / 360 * Math.PI * ROBOT_WIDTH * TICKS_PER_INCH);     //theta/360*PI*D
        leftTarget = encoderTarget;
        rightTarget = -encoderTarget;
        setTargetValueMotor();
        setMotorPower(.3, .3);

        if (hasReached()) {
            setMotorPower(0, 0);
            return true;
        }
        return false;
    }

    public boolean setTargetValueTurnRight(double degrees) {
        int encoderTarget = (int) (degrees / 360 * Math.PI * ROBOT_WIDTH * TICKS_PER_INCH);     //theta/360*PI*D
        leftTarget = encoderTarget;
        rightTarget = -encoderTarget;
        setTargetValueMotor();
        setMotorPower(.27, .33);

        if (hasReached()) {
            setMotorPower(0, 0);
            return true;
        }
        return false;
    }



    public void setTargetValueMotor() {
        frontLeft.setTargetPosition(leftTarget);
        backLeft.setTargetPosition(leftTarget);

        frontRight.setTargetPosition(rightTarget);
        backRight.setTargetPosition(rightTarget);
    }

    public boolean hasReached() {
        return (Math.abs(frontLeft.getCurrentPosition() - leftTarget) <= TOLERANCE &&
                Math.abs(backLeft.getCurrentPosition() - leftTarget) <= TOLERANCE &&
                Math.abs(frontRight.getCurrentPosition() - rightTarget) <= TOLERANCE &&
                Math.abs(backRight.getCurrentPosition() - rightTarget) <= TOLERANCE);
    }

    public void setMotorPower(double leftPower, double rightPower) {
        clipValues(leftPower, ComponentType.MOTOR);
        clipValues(rightPower, ComponentType.MOTOR);

        frontLeft.setPower(leftPower);
        backLeft.setPower(leftPower);

        frontRight.setPower(rightPower);
        backRight.setPower(rightPower);
    }

    //Propeller Manipulation
    public void alternatePropeller(boolean on){
        propeller.setTargetPosition(propellerTargetPos);
        propeller.setPower(.8);
        if (on) {
            if (propeller.getCurrentPosition() - PROPELLER_RIGHT <= 5) {
                propellerTargetPos = PROPELLER_LEFT;
            } else if (propeller.getCurrentPosition() - PROPELLER_LEFT >= -5) {
                propellerTargetPos = PROPELLER_RIGHT;
            }
        }
    }

    public void spinPropeller(int direction) {
        if (direction == 1) {
            propeller.setPower(1);
        } else if (direction == -1) {
            propeller.setPower(-1);
        } else if (direction == 0) {
            propeller.setPower(0);
        }
    }

    public boolean resetProp(){
        propeller.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        int currentPos = propeller.getCurrentPosition();
        if (targetPos==0) {
            targetPos = currentPos + (280 - (currentPos % 280));
        }
            propeller.setTargetPosition(targetPos);
            propeller.setPower(.2);
            if (targetPos - currentPos <= 2) {
                resetPropellerEncoder();
                propeller.setPower(0);
                targetPos=0;
                return true;
            } else return false;
    }

    public void resetPropellerEncoder(){
        resetProp();
        propeller.setMode(DcMotorController.RunMode.RUN_WITHOUT_ENCODERS);
    }

    public boolean setZipLinePosition(double pos) {//slider values
        if (pos == 1) {
            zipLiner.setPosition(SERVO_MAX);
        } else if (pos == -1) {
            zipLiner.setPosition(SERVO_MIN);
        } else if (pos == 0) {
            zipLiner.setPosition(SERVO_NEUTRAL);
        }
        telemetry.addData("00 Zipline moving at: ", pos);
        return true;
    }

    //HELPER METHODS
    enum ComponentType {
        NONE,
        MOTOR,
        SERVO
    }

    public double clipValues(double initialValue, ComponentType type) {
        double finalval = 0;
        if (type == ComponentType.MOTOR)
            finalval = Range.clip(initialValue, MOTOR_MIN, MOTOR_MAX);
        if (type == ComponentType.SERVO)
            finalval = Range.clip(initialValue, SERVO_MIN, SERVO_MAX);
        return finalval;
    }

    public void dropClimber(boolean dump) {
        if (dump) {
            dropClimber.setPosition(.5);
        }
        else if (!dump){
            dropClimber.setPosition(0);
        }
    }


    //DEBUG
    public void basicTel() {
        telemetry.addData("01 frontLeftPos: ", frontLeft.getCurrentPosition());
        telemetry.addData("02 backLeftPos: ", backLeft.getCurrentPosition());
        telemetry.addData("03 LeftTarget: ", leftTarget);

        telemetry.addData("04 frontRightPos: ", frontRight.getCurrentPosition());
        telemetry.addData("05 backRightPos: ", backRight.getCurrentPosition());
        telemetry.addData("06 RightTarget: ", rightTarget);

        telemetry.addData("07 ArmMotor1: ", armMotor1.getCurrentPosition());
        telemetry.addData("08 ArmMotor2: ", armMotor2.getCurrentPosition());
        telemetry.addData("09 propeller: ", propeller.getCurrentPosition());

        telemetry.addData("10 Target Position: ", targetPos);
        telemetry.addData("11 Drop Climber Position: ", dropClimber.getPosition());
        telemetry.addData("12 Back Bumper Pushed is ", backBumper.isPressed());

    }

    //MANDATORY METHODS
    public void loop() {
    }

    @Override
    public void stop() {
        telemetry.addData("Stop has started", backLeft.getPower());
        setMotorPower(0, 0);//brake the movement of drive
        setZipLinePosition(0);
        telemetry.addData("Stop has started", backLeft.getPower());
    }

}