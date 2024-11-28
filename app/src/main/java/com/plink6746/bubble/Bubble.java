package com.plink6746.bubble;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.plink6746.R;
import com.plink6746.engine.GameEngine;
import com.plink6746.engine.Sprite;
import com.plink6746.utils.ImageUtils;
import java.util.ArrayList;

public class Bubble extends Sprite {

    public final ArrayList<Bubble> mEdges = new ArrayList<>();
    public int mRow, mCol;
    public BubbleColor mBubbleColor;
    private GameEngine gameEngine;
    public int mDepth = -1;
    public boolean mDiscover = false;

    protected Bubble(GameEngine gameEngine, int row, int col, BubbleColor bubbleColor) {
        super(gameEngine, bubbleColor.getImageResId());
        this.gameEngine = gameEngine;
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
