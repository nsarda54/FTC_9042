package com.qualcomm.ftcrobotcontroller.opmodes;

public class MainTeleOp extends TeleOpHelper {

    public MainTeleOp() {

    }

    @Override
    public void loop() {
        basicTel();
        resetProp(reset);
        driveControl(driveType);

        //DRIVER CONTROLS
        //driving
        if (gamepad1.right_bumper) {
            driveType = "slow";
        }
        else if (gamepad1.left_bumper){
            driveType = "backwards";
        }
        else if (gamepad1.dpad_right && driveDropClimber(true)){
            driveType = "climber";
        }
        else {
            driveType = "normal";
        }

        //constant driving
        if (gamepad1.a) {
            setMotorPower(1, 1);
        }
        else if (gamepad1.y) {
            setMotorPower(-1 , -1);
        }

        //propeller
        if (gamepad1.left_trigger > 0) {
            spinPropeller(1);
        }
        else if (gamepad1.right_trigger > 0) {
            spinPropeller(-1);
        }
        else if (gamepad1.b) {
            reset = true;
        }
        else {
            spinPropeller(0);
        }

        //shelter climber drop
        if (gamepad1.dpad_up){
            dropClimber(true);
        }
        else if (gamepad1.dpad_down){
            dropClimber(false);
        }

        //OPERATOR CONTROLS
        //zipliners

        if (gamepad2.right_bumper) {
            setZipLinePosition(1);
        }
        else if (gamepad2.left_bumper) {
            setZipLinePosition(-1);
        }
        else {
            setZipLinePosition(0);
        }

        //arm
        if (gamepad2.dpad_down) {
            setArmPivot(-.7);
        }
        else if (gamepad2.dpad_up) {
            setArmPivot(.7);
        }
        else {
            setArmPivot(0);
        }

        //tube
        if (gamepad2.y) {
            moveTubing(1);
        }
        else if (gamepad2.a) {
            moveTubing(-1);
        }
        else {
            moveTubing(0);
        }
    }
}