package com.BibaSpirt.bubble;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.BibaSpirt.engine.GameEngine;
import com.BibaSpirt.engine.GameEvent;
import com.BibaSpirt.engine.GameObject;
import com.BibaSpirt.engine.Sprite;
import com.BibaSpirt.fragment.Observers;

import java.util.Random;

/**
 * Player class represents the player's bubble shooter.
 * It extends the Sprite class and overrides its methods to handle player-specific logic.
 */
public class Player extends Sprite {

    public BubbleColor mBubbleColor;
    private final BubbleManager mBubbleManager;

    private final float mStartX, mStartY;
    private final float mMaxX;
    private final float mSpeed;
    private float mSpeedX, mSpeedY;
    private boolean mShoot = false;
    private int pos = 0;
    Observers gameOver;

    private final Random mRandom = new Random();

    /**
     * Constructor for the Player class.
     * Initializes the player's bubble shooter with the given game engine, bubble manager, and game over observer.
     *
     * @param gameEngine   The game engine instance.
     * @param bubbleManager The bubble manager instance.
     * @param gameOver      The game over observer instance.
     */
    public Player(GameEngine gameEngine, BubbleManager bubbleManager, Observers gameOver) {
        super(gameEngine, BubbleColor.BLUE.getImageResId());
        this.gameOver = gameOver;

        mBubbleColor = BubbleColor.BLUE;
        mBubbleManager = bubbleManager;

        // Position the ball at the bottom center
        mStartX = mScreenWidth / 2f;
        mStartY = mScreenHeight - 85f;

        mMaxX = gameEngine.mScreenWidth - mWidth;
        mSpeed = gameEngine.mPixelFactor * 3000 / 1000;

        gameEngine.addGameObject(new BubblePath(), 2);
    }

    @Override
    public void startGame() {
        // Move slightly to the left by adding an offset
        float offset = mWidth * 0.1f;  // 10% of width as offset
        mX = (mScreenWidth / 2f) - (mWidth / 2f) - offset;
        mY = mStartY - mHeight;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        // We shoot the bubble one time
        if (mShoot) {
            // We convert angle to x speed and y speed
            float sideX = gameEngine.mInputController.mXUp - mStartX;
            float sideY = gameEngine.mInputController.mYUp - mStartY;
            float angle = (float) Math.atan2(sideY, sideX);

            mSpeedX = (float) (mSpeed * Math.cos(angle));
            mSpeedY = (float) (mSpeed * Math.sin(angle));

            mShoot = false;
        }

        // Update position
        mX += mSpeedX * elapsedMillis;
        if (mX <= 0) {
            mX = 0;
            mSpeedX = -mSpeedX;
        }
        if (mX >= mMaxX) {
            mX = mMaxX;
            mSpeedX = -mSpeedX;
        }

        mY += mSpeedY * elapsedMillis;
        if (mY <= -mHeight || mY >= mScreenHeight) {   // Player out of screen
            setNextBubble();
        }
    }

    @Override
    public void onGameEvent(GameEvent gameEvent) {
        if (gameEvent == GameEvent.SHOOT) {
            mShoot = true;
        }
    }

    @Override
    public void onCollision(GameEngine gameEngine, Sprite otherObject) {
        if (otherObject instanceof Bubble) {
            Bubble bubble = (Bubble) otherObject;
            if (bubble.mBubbleColor != BubbleColor.BLANK && mY >= bubble.mY) {
                try {
                    mBubbleManager.addBubble(this, bubble);
                } catch (ArrayIndexOutOfBoundsException e) {
                    e.printStackTrace();
                    if (e.getMessage().equals("length=9; index=9")) {
                        gameOver.observeGameOver(true);
                    }
                }
                setNextBubble();
            }
        }
    }

    private void setNextBubble() {
        Log.d("@@@", "setNextBubble: " + pos);

        if (pos > 5) {
            pos = 0;
        }
        // Set random color to next bubble
        BubbleColor color = BubbleColor.values()[pos];
        pos++;
        mBubbleColor = color;
        mBitmap = getDefaultBitmap(mResources.getDrawable(color.getImageResId()));

        // Reset Position with the same offset
        float offset = mWidth * 0.1f;  // 10% of width as offset
        mSpeedX = 0;
        mSpeedY = 0;
        mX = (mScreenWidth / 2f) - (mWidth / 2f) - offset;
        mY = mStartY - mHeight;
    }

    class BubblePath extends GameObject {

        private final float mMaxX, mMinX;
        private final float mRadius;
        private final float BUBBLE_SCALE = 0.52f;  // Match the scale in Sprite class
        private float mReflectX, mReflectY;
        private float mEndX, mEndY;
        private boolean mDraw = false;
        private final Paint mPaint = new Paint();

        public BubblePath() {
            // Use screen edges for the aiming line
            mMaxX = mScreenWidth;
            mMinX = 0;
            // Remove the 0.9f factor to make line longer
            mRadius = mScreenWidth / 2f;
        }

        @Override
        public void startGame() {
        }

        private float getBallCenterX() {
            float finalScale = mPixelFactor * BUBBLE_SCALE * 0.72f;
            float scaledWidth = mWidth * finalScale;
            return mX + (mWidth * 0.02f) + (scaledWidth * 1.4f);
        }

        private float getBallCenterY() {
            float finalScale = mPixelFactor * BUBBLE_SCALE * 0.72f;
            float scaledHeight = mHeight * finalScale;
            return mY + (mHeight * 0.02f) + (scaledHeight * 1.4f);
        }

        private void calculateAimingLine(float touchX, float touchY, float startX, float startY) {
            // Calculate direction vector
            float dirX = touchX - startX;
            float dirY = touchY - startY;
            
            // Normalize the direction vector
            float length = (float) Math.sqrt(dirX * dirX + dirY * dirY);
            dirX /= length;
            dirY /= length;
            
            // Calculate intersection with top wall (y = 0)
            float t = -startY / dirY;
            float intersectX = startX + dirX * t;
            
            // If intersection point is outside screen bounds, calculate wall reflection
            if (intersectX < 0 || intersectX > mScreenWidth) {
                // Hit side wall first
                float wallX = (dirX > 0) ? mScreenWidth : 0;
                t = (wallX - startX) / dirX;
                mReflectX = wallX;
                mReflectY = startY + dirY * t;
                
                // Calculate reflection vector
                float reflectDirX = -dirX;
                float reflectDirY = dirY;
                
                // Calculate second intersection point
                float t2 = -mReflectY / reflectDirY;
                mEndX = mReflectX + reflectDirX * t2;
                mEndY = 0;
                
                // Clamp end point to screen bounds
                if (mEndX < 0) mEndX = 0;
                if (mEndX > mScreenWidth) mEndX = mScreenWidth;
            } else {
                // Direct shot to top
                mReflectX = intersectX;
                mReflectY = 0;
                mEndX = intersectX;
                mEndY = 0;
            }
        }

        @Override
        public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
            if (gameEngine.mInputController.mAiming) {
                float ballCenterX = getBallCenterX();
                float ballCenterY = getBallCenterY();
                float touchX = gameEngine.mInputController.mXDown;
                float touchY = gameEngine.mInputController.mYDown;
                
                // Only calculate if touch point is above the ball
                if (touchY < ballCenterY) {
                    calculateAimingLine(touchX, touchY, ballCenterX, ballCenterY);
                    mDraw = true;
                } else {
                    mDraw = false;
                }
            } else {
                mDraw = false;
            }
        }

        @Override
        public void onDraw(Canvas canvas) {
            if (!mDraw) {
                return;
            }

            mPaint.setColor(Color.parseColor("#01defe"));
            mPaint.setStrokeWidth(1);  // Made line thinner
            mPaint.setAntiAlias(true); // Keep anti-aliasing for smoothness
            
            float ballCenterX = getBallCenterX();
            float ballCenterY = getBallCenterY();
            canvas.drawLine(ballCenterX, ballCenterY, mReflectX, mReflectY, mPaint);
            if (mReflectY > 0) { // Only draw second line if there's a reflection
                canvas.drawLine(mReflectX, mReflectY, mEndX, mEndY, mPaint);
            }
        }
    }
}
