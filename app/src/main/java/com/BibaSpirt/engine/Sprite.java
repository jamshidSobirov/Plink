package com.BibaSpirt.engine;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.BibaSpirt.sound.SoundManager;

public abstract class Sprite extends GameObject {

    private static final boolean DEBUG_MODE = false;

    protected final int mScreenWidth;
    protected final int mScreenHeight;
    protected final int mWidth;
    protected final int mHeight;
    private final float mRadius;

    public float mX;
    public float mY;
    public float mRotation;
    public float mScale = 1f;
    public int mAlpha = 255;

    protected final float mPixelFactor;
    protected Bitmap mBitmap;
    private final Matrix mMatrix = new Matrix();
    private final Paint mPaint = new Paint();
    public final Rect mBoundingRect = new Rect(-1, -1, -1, -1);

    protected final Resources mResources;
    protected final SoundManager mSoundManager;

    protected Sprite(GameEngine gameEngine, int drawableRes) {
        mResources = gameEngine.getContext().getResources();
        mBitmap = getDefaultBitmap(mResources.getDrawable(drawableRes));
        mPixelFactor = gameEngine.mPixelFactor;

        mScreenWidth = gameEngine.mScreenWidth;
        mScreenHeight = gameEngine.mScreenHeight;
        mWidth = (int) (mBitmap.getWidth() * mPixelFactor);
        mHeight = (int) (mBitmap.getHeight() * mPixelFactor);
        // Reduce the collision radius to account for padding
        mRadius = Math.max(mHeight, mWidth) * 0.4f;  

        mSoundManager = gameEngine.mSoundManager;
    }

    protected Bitmap getDefaultBitmap(Drawable drawable) {
        float COLORED_BUBBLE_SCALE = 0.52f;  // Scale for colored bubbles
        float BLANK_BUBBLE_SCALE = 0.54f;    // Scale for blank bubbles
        
        if (drawable instanceof BitmapDrawable) {
            Bitmap original = ((BitmapDrawable) drawable).getBitmap();
            
            // Create a cropped bitmap without padding
            int size = Math.min(original.getWidth(), original.getHeight());
            Bitmap croppedBitmap = Bitmap.createBitmap(
                original,
                (original.getWidth() - size) / 2,
                (original.getHeight() - size) / 2,
                size,
                size
            );
            
            int newWidth = (int)(croppedBitmap.getWidth() * COLORED_BUBBLE_SCALE);
            int newHeight = (int)(croppedBitmap.getHeight() * COLORED_BUBBLE_SCALE);
            return Bitmap.createScaledBitmap(croppedBitmap, newWidth, newHeight, true);
        } else {
            // Create a fully transparent bubble bitmap
            int size = 360;
            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.TRANSPARENT);
            paint.setAlpha(0);
            
            float radius = size / 2f;
            canvas.drawCircle(radius, radius, radius, paint);
            
            int newWidth = (int)(size * BLANK_BUBBLE_SCALE);
            int newHeight = (int)(size * BLANK_BUBBLE_SCALE);
            return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        }
    }

    public boolean checkCollision(Sprite otherSprite) {
        double distanceX = (mX + mWidth / 2f) - (otherSprite.mX + otherSprite.mWidth / 2f);
        double distanceY = (mY + mHeight / 2f) - (otherSprite.mY + otherSprite.mHeight / 2f);
        double squareDistance = distanceX * distanceX + distanceY * distanceY;
        // Add a small buffer to prevent premature collisions
        double collisionDistance = (mRadius + otherSprite.mRadius) * 0.95;  
        return squareDistance <= collisionDistance * collisionDistance;
    }

    public void onCollision(GameEngine gameEngine, Sprite otherObject) {
    }

    @Override
    public void onPostUpdate() {
        mBoundingRect.set(
                (int) mX,
                (int) mY,
                (int) mX + mWidth,
                (int) mY + mHeight);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mX > canvas.getWidth() || mY > canvas.getHeight() || mX < -mWidth || mY < -mHeight) {
            return;
        }

        if (DEBUG_MODE) {
            mPaint.setColor(Color.BLACK);
            canvas.drawRect(mBoundingRect, mPaint);
        }

        float scaleFactor = mPixelFactor * mScale * 0.72f;
        mMatrix.reset();
        mMatrix.postScale(scaleFactor, scaleFactor);
        mMatrix.postTranslate(mX + (mWidth * 0.02f), mY + (mHeight * 0.02f));  
        mMatrix.postRotate(mRotation, mX + mWidth / 2f, mY + mHeight / 2f);
        mPaint.setAlpha(mAlpha);
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
    }

}
