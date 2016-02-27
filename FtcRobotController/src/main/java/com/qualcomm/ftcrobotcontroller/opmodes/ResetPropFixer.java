package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotorController;

/**
 * Created by Tim on 10/25/2015.
 */
//STARTING POSITION = Middle on crack of 2 Mats from side non mountain corner

public class ResetPropFixer extends AutonHelper{


    //establish run states for auton
    enum RunState{
        RESET_STATE,
        FIRST_STATE,
        LAST_STATE
    }


    private RunState rs = RunState.RESET_STATE;

    public ResetPropFixer() {}


    @Override
    public void loop() {

        basicTel();
        telemetry.addData("state: ", rs);
        if (propeller.getMode()!= DcMotorController.RunMode.RUN_TO_POSITION){
            propeller.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        }
        alternatePropeller(on);

        switch(rs) {
            case RESET_STATE:
            {
                on = true;
                if (time>10){
                    rs= RunState.FIRST_STATE;
                }
                break;
            }
            case FIRST_STATE:
            {
                on = false;
                if (resetProp()){
                    rs = RunState.LAST_STATE;
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