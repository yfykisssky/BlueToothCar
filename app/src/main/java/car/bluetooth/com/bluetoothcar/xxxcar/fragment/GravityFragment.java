package car.bluetooth.com.bluetoothcar.xxxcar.fragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Switch;

import car.bluetooth.com.bluetoothcar.xxxcar.R;
import car.bluetooth.com.bluetoothcar.xxxcar.view.RockerView;

public class GravityFragment extends BaseFragment {

    private int speedScale;

    private ProgressBar speedProgress;

    private SensorManager sensorManager;
    private SensorEventListener shakeListener;
    private Context context;
    private Switch mSwitch;
    private Sensor mAccelerometerSensor;
    private RockerView rockerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gravity, null);

        context = this.getActivity();
        sensorManager = (SensorManager) this.getActivity().getSystemService(Context.SENSOR_SERVICE);
        mAccelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeListener = new ShakeSensorListener();

        initView(view);

        return view;
    }

    private void initView(View view) {

        speedProgress = view.findViewById(R.id.speed_progress);

        rockerView = view.findViewById(R.id.rockerview);
        rockerView.setCanTouch(false);

    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(shakeListener, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // 务必要在pause中注销 mSensorManager
        // 否则会造成界面退出后摇一摇依旧生效的bug
        if (sensorManager != null) {
            sensorManager.unregisterListener(shakeListener);
        }
        super.onPause();
    }

    private class ShakeSensorListener implements SensorEventListener {

        private static final double MAX_VALUE = 9.8;

        @Override
        public void onSensorChanged(SensorEvent event) {

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {//如果是加速度传感器
                float[] values = event.values;

                float x = values[0];
                float y = values[1];
                float z = values[2];
                Log.e("out", "x" + x + "y" + y);

                rockerView.moveScaleRocker((float) (x + MAX_VALUE), (float) (y + MAX_VALUE), (float) MAX_VALUE);
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

    }

    private void toSpeedScale(float scale) {

    }

}



