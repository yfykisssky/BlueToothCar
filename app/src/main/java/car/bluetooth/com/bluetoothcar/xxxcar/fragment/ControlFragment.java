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

import com.larswerkman.holocolorpicker.ColorPicker;

import car.bluetooth.com.bluetoothcar.xxxcar.R;
import car.bluetooth.com.bluetoothcar.xxxcar.view.RockerView;

public class ControlFragment extends BaseFragment {

    private int speedScale;

    private ProgressBar speedProgress;

    private ColorPicker picker;

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

        picker = view.findViewById(R.id.picker);

        picker.setShowOldCenterColor(false);

        picker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(int color) {

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
