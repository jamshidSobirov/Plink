package com.plink6746.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

public class ImageUtils {
    
    public static Drawable getScaledDrawable(Context context, int resourceId) {
        // Load the original drawable
        Drawable drawable = context.getResources().getDrawable(resourceId);
        
        // Convert 20dp to pixels (matching the ImageView size in layout)
        int targetSize = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            20,  // 20dp from layout
            context.getResources().getDisplayMetrics()
        );
        
        // Set bounds for the drawable
        drawable.setBounds(0, 0, targetSize, targetSize);
        
        // Create a bitmap to draw into
        Bitmap bitmap = Bitmap.createBitmap(targetSize, targetSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        
        // Create and return the drawable
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public static Bitmap getBitmapFromDrawable(Context context, int resourceId) {
        // Load the drawable
        Drawable drawable = context.getResources().getDrawable(resourceId);
        
        // Convert 20dp to pixels (matching the ImageView size in layout)
        int targetSize = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            20,  // 20dp from layout
            context.getResources().getDisplayMetrics()
        );
        
        // Set bounds for the drawable
        drawable.setBounds(0, 0, targetSize, targetSize);
        
        // Create a bitmap to draw into
        Bitmap bitmap = Bitmap.createBitmap(targetSize, targetSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        
        return bitmap;
    }
}
