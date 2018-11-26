package car.bluetooth.com.bluetoothcar.xxxcar.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import car.bluetooth.com.bluetoothcar.xxxcar.R;

public class AnimView extends android.support.v7.widget.AppCompatImageView {

    public AnimView(Context context) {
        super(context);
        initView(context);
    }

    public AnimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.setBackgroundColor(Color.parseColor("#F0000000"));
        this.setImageResource(R.drawable.avoid);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.img_animation);
        LinearInterpolator lin = new LinearInterpolator();
        animation.setInterpolator(lin);
        this.startAnimation(animation);
    }

}

