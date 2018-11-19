package car.bluetooth.com.bluetoothcar.xxxcar.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.larswerkman.holocolorpicker.ColorPicker;

import car.bluetooth.com.bluetoothcar.xxxcar.R;
import car.bluetooth.com.bluetoothcar.xxxcar.view.ColorPickerView;
import car.bluetooth.com.bluetoothcar.xxxcar.view.RockerView;

public class ControlFragment extends BaseFragment {

    private int speedScale;

    private ProgressBar speedProgress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_control, null);

        initView(view);

        return view;
    }

    private void initView(View view) {

        speedProgress = view.findViewById(R.id.speed_progress);

        LinearLayout colorLayout = view.findViewById(R.id.layout_color);

        ColorPickerView cpv = view.findViewById(R.id.picker);
        cpv.setOnColorChangedListenner(new ColorPickerView.OnColorChangedListener() {
            @Override
            public void onColorChanged(int r, int g, int b) {
                if (r == 0 && g == 0 && b == 0) {
                    return;
                }
                //Toast.makeText(MyViewActivity.this, "选取 RGB:"+r+","+g+","+b, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMoveColor(int r, int g, int b) {
                if (r == 0 && g == 0 && b == 0) {
                    return;
                }
                // tv_rgb.setText("RGB:" + r + "," + g + "," + b);
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
}
