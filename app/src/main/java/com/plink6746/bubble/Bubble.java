package com.plink6746.bubble;

import com.plink6746.engine.GameEngine;
import com.plink6746.engine.Sprite;
import java.util.ArrayList;

public class Bubble extends Sprite {

    public final int mRow, mCol;
    public BubbleColor mBubbleColor;
    public int mDepth = -1;
    public boolean mDiscover = false;
    public final ArrayList<Bubble> mEdges = new ArrayList<>(6);

    protected Bubble(GameEngine gameEngine, int row, int col, BubbleColor bubbleColor) {
        super(gameEngine, bubbleColor.getImageResId());

        mRow = row;
        mCol = col;
        mBubbleColor = bubbleColor;
    }

    @Override
    public void startGame() {
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {

    }

    public void setBubbleColor(BubbleColor color) {
        mBubbleColor = color;
        mBitmap = getDefaultBitmap(mResources.getDrawable(color.getImageResId()));
    }

    public void setBlankBubble() {
        setBubbleColor(BubbleColor.BLANK);
    }

}
