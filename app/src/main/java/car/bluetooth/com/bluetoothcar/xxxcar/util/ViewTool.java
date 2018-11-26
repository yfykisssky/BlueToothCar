package car.bluetooth.com.bluetoothcar.xxxcar.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.widget.RadioButton;

import car.bluetooth.com.bluetoothcar.xxxcar.R;

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

    public static void setRadioButtonRect(RadioButton rb, Context context) {
        int width = dip2px(context, (180 - 10) / 2);
        //挨着给每个RadioButton加入drawable限制边距以控制显示大小
        Drawable[] drawables = rb.getCompoundDrawables();
        //获取drawables
        Rect r = new Rect(0, 0, width, width);
        //定义一个Rect边界
        drawables[1].setBounds(r);
        //添加限制给控件
        rb.setCompoundDrawables(null, drawables[1], null, null);
    }

    public static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
