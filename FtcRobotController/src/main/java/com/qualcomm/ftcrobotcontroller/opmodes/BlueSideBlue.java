package com.qualcomm.ftcrobotcontroller.opmodes;

/**
 * Created by Tim on 10/25/2015.
 */
//STARTING POSITION = Middle on crack of 2 Mats from side non mountain corner

public class BlueSideBlue extends AutonHelper{


    //establish run states for auton
    enum RunState{
        RESET_STATE,
        FIRST_STATE,
        FIRST_RESET,
        SECOND_STATE,
        SECOND_RESET,
        THIRD_STATE,
        THIRD_RESET,
        FOURTH_STATE,
        FOURTH_RESET,
        FIFTH_STATE,
        FIFTH_RESET,
        SIXTH_STATE,
        SIXTH_RESET,
        SEVENTH_STATE,
        LAST_STATE,
        RESET_PROP
    }


    private RunState rs = RunState.RESET_STATE;

    public BlueSideBlue() {}


    @Override
    public void loop() {
//        setZipLinePosition(0);
        basicTel();
        setToEncoderMode();

        switch(rs) {
            case RESET_STATE:
            {
                spinPropeller(1);
                setZipLinePosition(0);
                resetEncoders();
                rs=RunState.FIRST_STATE;
                break;
            }
            case FIRST_STATE:
            {
                if(runStraight(-12, false) ){
                    rs = RunState.FIRST_RESET;
                }
                break;
            }
            case FIRST_RESET: {

                if(resetEncoders()){//make sure that the encoder have reset
                    rs = RunState.SECOND_STATE;
                }
                break;
            }
            case SECOND_STATE: {
                if (setTargetValueTurn(90)){
                    rs = RunState.SECOND_RESET;
                }
                break;
            }
            case SECOND_RESET:
            {
                if(resetEncoders()){//make sure that the encoder have reset
                    rs = RunState.THIRD_STATE;
                }
                break;
            }
            case THIRD_STATE:
            {
                if (runStraight(-70, false)){
                    rs= RunState.THIRD_RESET;
                }
                break;
            }
            case THIRD_RESET:
            {
                if (resetEncoders()){
                    rs= RunState.FOURTH_STATE;
                }
                break;
            }
            case FOURTH_STATE: {
                setZipLinePosition(-1);
                if (setTargetValueTurn(145)){
                    rs= RunState.FOURTH_RESET;
                }
                break;
            }
            case FOURTH_RESET:
            {
                setZipLinePosition(0);
                if (resetEncoders()){
                    rs= RunState.FIFTH_STATE;
                }
                break;
            }
            case FIFTH_STATE:
            {
                if (runStraight(-20, false)){
                    rs= RunState.FIFTH_RESET;
                }
                break;
            }
            case FIFTH_RESET:
            {
                spinPropeller(0);
                if (resetEncoders()){
                    rs= RunState.RESET_PROP;
                }
                break;
            }
            case RESET_PROP:
            {
                if (resetProp()){
                    rs=RunState.SIXTH_STATE;
                }
                break;
            }
            case SIXTH_STATE:
            {
                if (runStraight(-50, false)){
                    rs= RunState.SIXTH_RESET;
                }
                break;
            }
            case SIXTH_RESET:
            {
                if (resetEncoders()){
                    rs= RunState.SEVENTH_STATE;
                }
                break;
            }
            case SEVENTH_STATE:
            {
                if (runStraight(-10, true)){
                    rs=RunState.LAST_STATE;
                }
                break;
            }
            case LAST_STATE:
            {
                stop();
            }
        }
    }
}