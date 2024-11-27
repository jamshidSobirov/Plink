package com.plink6746.engine;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class RedLine extends GameObject {
    private final Paint mPaint;
    private final float mY;
    private final float mScreenWidth;

    public RedLine(GameEngine gameEngine, float y) {
        mScreenWidth = gameEngine.mScreenWidth;
        mY = y;
        
        // Initialize paint for red line
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5); // Line thickness
    }

    @Override
    public void startGame() {}

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {}

    @Override
    public void onDraw(Canvas canvas) {
        // Draw a red line across the screen
        canvas.drawLine(0, mY, mScreenWidth, mY, mPaint);
    }
}
