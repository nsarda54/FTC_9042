package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Tim on 10/25/2015.
 */
//STARTING POSITION = Middle on crack of 2 Mats from side non mountain corner

public class RollRolatubeIn extends AutonHelper{


    //establish run states for auton
    enum RunState{
        RESET_STATE,
        FIRST_STATE,
        LAST_STATE
    }


    private RunState rs = RunState.RESET_STATE;

    public RollRolatubeIn() {}


    @Override
    public void loop() {

        basicTel();
        telemetry.addData("state: ", rs);
        setToEncoderMode();

        switch(rs) {
            case RESET_STATE:
            {
                setZipLinePosition(0);
                if (resetEncoders()){
                    rs= RunState.FIRST_STATE;
                }
                break;
            }
            case FIRST_STATE:
            {
                armMotor1.setPower(-1);
                armMotor2.setPower(-1);
            }
            case LAST_STATE:
            {
                on=false;
                stop();
            }
        }
    }
}