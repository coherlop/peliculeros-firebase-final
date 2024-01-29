package com.example.nuevofirebasepeliculas.utilidades;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class ImagenesBlobBitmap {

    // mas información aquí de cómo comprimir una imagen a una resolución aquí
    // -> https://developer.android.com/topic/performance/graphics/load-bitmap#java
    // los siguientes tres métodos están sacado de esta página
    //------------------------------------------------------------------------------------------------------------
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    //método que convierte byte[] a bitmap
    public static Bitmap bytes_to_bitmap(byte[] b, int width, int height){
        Bitmap.Config config = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(width, height,config);
        try{
            bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        } catch (Exception e){
        }
        return bitmap;
    }

    public static byte[] bitmap_to_bytes_png(Bitmap foto_bitmap)
    {
        if(foto_bitmap != null)
        {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            foto_bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bArray = bos.toByteArray();
            return bArray;
        }
        return null;
    }

}
