package com.cfp.muaavin.helper;

import android.graphics.PixelFormat;

/**
 * Created by KMajeed on 7/13/2017.
 */

public class border extends android.graphics.drawable.Drawable
        {
           public android.graphics.Paint paint;
            public android.graphics.Rect bounds_rect;

            public border(int colour, int width)
            {
              this.paint = new android.graphics.Paint();
             this.paint.setColor(colour);
             this.paint.setStrokeWidth(width);
             this.paint.setStyle(android.graphics.Paint.Style.STROKE);
           }

           @Override
   public void onBoundsChange(android.graphics.Rect bounds)
          {
            this.bounds_rect = bounds;
          }

          public void draw(android.graphics.Canvas c)
          {
             c.drawRect(this.bounds_rect, this.paint);
           }

           public void setAlpha(int a)
           {
             // TODO: Implement this method
           }

           public void setColorFilter(android.graphics.ColorFilter cf)
           {
             // TODO: Implement this method
           }

           public int getOpacity()
           {
             // TODO: Implement this method
             return PixelFormat.TRANSLUCENT;
           }
         }