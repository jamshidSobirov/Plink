package com.plink6746.bubble;

import com.plink6746.R;

public enum BubbleColor {
    BLUE, RED, YELLOW, ORANGE, BLACK, GREEN, BLANK;

    public int getImageResId() {

        switch (this) {
            case BLUE:
                return R.drawable.ball1;
            case RED:
                return R.drawable.ball2;
            case YELLOW:
                return R.drawable.ball3;
            case ORANGE:
                return R.drawable.ball4;
            case BLACK:
                return R.drawable.ball5;
            case GREEN:
                return R.drawable.ball6;
            case BLANK:
                return R.drawable.black_bubble;
        }

        return 0;
    }
}
