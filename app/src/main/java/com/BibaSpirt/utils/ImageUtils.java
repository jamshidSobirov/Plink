package com.BibaSpirt.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

public class ImageUtils {
    
    public static Drawable getScaledDrawable(Context context, int resourceId) {
        // Load the original bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false; // Don't pre-scale the image
        Bitmap originalBitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
        
        // Convert 20dp to pixels (matching the ImageView size in layout)
        int targetSize = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            20,  // 20dp from layout
            context.getResources().getDisplayMetrics()
        );
        
        // Scale the bitmap
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(
            originalBitmap,
            targetSize,
            targetSize,
            true // Use bilinear filtering for better quality
        );
        
        // Clean up the original bitmap if it's different from the scaled one
        if (originalBitmap != scaledBitmap) {
            originalBitmap.recycle();
        }
        
        // Create and return the drawable
        return new BitmapDrawable(context.getResources(), scaledBitmap);
    }
}
