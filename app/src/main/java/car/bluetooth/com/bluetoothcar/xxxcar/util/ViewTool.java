package car.bluetooth.com.bluetoothcar.xxxcar.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.DisplayMetrics;

public class ViewTool {

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm2 = context.getResources().getDisplayMetrics();
        return dm2.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm2 = context.getResources().getDisplayMetrics();
        return dm2.heightPixels;
    }

    public static Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);// 使用后乘
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }

}
