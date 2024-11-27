package com.plink6746.inputcontroller;

import android.view.MotionEvent;
import android.view.View;

import com.plink6746.R;
import com.plink6746.engine.GameEngine;
import com.plink6746.engine.GameEvent;
import com.plink6746.engine.InputController;


/**
 * Created by Oscar Liang on 2022/07/08
 */

public class BasicInputController extends InputController {

    private final GameEngine mGameEngine;

    public BasicInputController(GameEngine gameEngine) {
        mGameEngine = gameEngine;
        gameEngine.mActivity.findViewById(R.id.game_view).setOnTouchListener(new BasicOnTouchListener());
    }

    private class BasicOnTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                mAiming = true;
                mXDown = (int) event.getX();
                mYDown = (int) event.getY();
                // Log.d("input", "(" + mXDown + ", " + mYDown + ")");
            } else if (action == MotionEvent.ACTION_MOVE) {
                mXDown = (int) event.getX();
                mYDown = (int) event.getY();
                // Log.d("input", "(" + mXDown + ", " + mYDown + ")");
            } else if (action == MotionEvent.ACTION_UP) {
                mAiming = false;
                mXUp = (int) event.getX();
                mYUp = (int) event.getY();
                // Log.d("input", "(" + mXUp + ", " + mYUp + ")");

                mGameEngine.onGameEvent(GameEvent.SHOOT);
            }

            return true;
        }
    }

}
