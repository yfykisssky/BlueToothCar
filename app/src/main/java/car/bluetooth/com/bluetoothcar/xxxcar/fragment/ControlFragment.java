package car.bluetooth.com.bluetoothcar.xxxcar.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.larswerkman.holocolorpicker.ColorPicker;

import car.bluetooth.com.bluetoothcar.xxxcar.R;
import car.bluetooth.com.bluetoothcar.xxxcar.view.ColorPickerView;
import car.bluetooth.com.bluetoothcar.xxxcar.view.RockerView;

public class ControlFragment extends BaseFragment implements View.OnClickListener {

    private int speedScale;

    private ProgressBar speedProgress;

    private TextView colorTex;

    private LinearLayout colorLay;

    private LinearLayout soundLay;

    private LinearLayout faceLay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_control, null);

        initView(view);

        return view;
    }

    private void initView(View view) {

        speedProgress = view.findViewById(R.id.speed_progress);

        colorLay = view.findViewById(R.id.layout_color);
        soundLay = view.findViewById(R.id.layout_sound);
        faceLay = view.findViewById(R.id.layout_face);

        view.findViewById(R.id.color).setOnClickListener(this);
        view.findViewById(R.id.sound).setOnClickListener(this);
        view.findViewById(R.id.face).setOnClickListener(this);

        colorTex = view.findViewById(R.id.color_show);

        colorTex.setBackgroundColor(Color.WHITE);

        ColorPickerView cpv = view.findViewById(R.id.picker);
        cpv.setOnColorChangedListenner(new ColorPickerView.OnColorChangedListener() {
            @Override
            public void onColorChanged(int r, int g, int b) {
                if (r == 0 && g == 0 && b == 0) {
                    return;
                }
            }

            @Override
            public void onMoveColor(int r, int g, int b) {
                if (r == 0 && g == 0 && b == 0) {
                    return;
                }
                colorTex.setBackgroundColor(Color.rgb(r, g, b));
            }
        });

        RockerView rockerView = view.findViewById(R.id.rockerview);
        if (rockerView != null) {
            rockerView.setCallBackMode(RockerView.CallBackMode.CALL_BACK_MODE_STATE_CHANGE);
       /*     rockerView.setOnShakeListener(RockerView.DirectionMode.DIRECTION_8, new RockerView.OnShakeListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void direction(RockerView.Direction direction) {
                    // directionState = direction;
                }

                @Override
                public void onFinish() {
                    //directionState = null;
                }
            });*/
            rockerView.setOnAngleChangeListener(new RockerView.OnAngleChangeListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void angle(double angle) {

                    Log.e("angle", String.valueOf(angle));

                }

                @Override
                public void onFinish() {

                }
            });
        }

        rockerView.setOnLenXYListener(new RockerView.OnLenXYListener() {
            @Override
            public void lenXY(float lenXY, float radus) {
                speedScale = (int) (lenXY * 100 / radus);
                speedProgress.setProgress(speedScale);
            }
        });

    }

    private void toSpeed(double angle) {
      /*  double leftWheelSpeed = 0;
        double leftRightSpeed = 0;
        if (angle >= 0 || angle <= 90) {
            leftWheelSpeed = angle / 90 * speedScale * 255;
            leftRightSpeed = (90 - angle) / 90 * speedScale * 255;
        } else if (angle <= 180 || angle > 90) {
            leftWheelSpeed = (180 - angle) / 180 * speedScale * 255;
            leftRightSpeed = angle / 180 * speedScale * 255;
        }*/

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.color:
                colorLay.setVisibility(View.VISIBLE);
                soundLay.setVisibility(View.GONE);
                faceLay.setVisibility(View.GONE);
                break;
            case R.id.sound:
                colorLay.setVisibility(View.GONE);
                soundLay.setVisibility(View.VISIBLE);
                faceLay.setVisibility(View.GONE);
                break;
            case R.id.face:
                colorLay.setVisibility(View.GONE);
                soundLay.setVisibility(View.GONE);
                faceLay.setVisibility(View.VISIBLE);
                break;
        }
    }
}
