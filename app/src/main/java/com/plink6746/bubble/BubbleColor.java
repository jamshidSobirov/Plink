package com.plink6746.bubble;

import com.plink6746.R;

public enum BubbleColor {
    BLUE, RED, YELLOW, ORANGE, BLACK, GREEN, BLANK;

    public int getImageResId() {

        switch (this) {
            case BLUE:
                return R.drawable.ball_blue;
            case RED:
                return R.drawable.ball_red;
            case YELLOW:
                return R.drawable.ball_yellow;
            case ORANGE:
                return R.drawable.ball_orange;
            case BLACK:
                return R.drawable.ball_black;
            case GREEN:
                return R.drawable.ball_green;
            case BLANK:
                return R.drawable.black_bubble;
        }

        return 0;
    }
}
