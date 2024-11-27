package com.plink6746.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;

public class DrawableUtils {
    public static BitmapDrawable createTransparentBubble(Context context, int size) {
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setAlpha(2); // 0.08 * 255 ≈ 20
        
        float radius = size / 2f;
        canvas.drawCircle(radius, radius, radius, paint);
        
        return new BitmapDrawable(context.getResources(), bitmap);
    }
}
