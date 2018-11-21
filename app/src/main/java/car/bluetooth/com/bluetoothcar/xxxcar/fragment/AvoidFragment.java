package car.bluetooth.com.bluetoothcar.xxxcar.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import car.bluetooth.com.bluetoothcar.xxxcar.R;
import car.bluetooth.com.bluetoothcar.xxxcar.activity.MainActivity;
import car.bluetooth.com.bluetoothcar.xxxcar.util.OrderCode;
import car.bluetooth.com.bluetoothcar.xxxcar.view.RockerView;

public class AvoidFragment extends BaseFragment implements View.OnClickListener {

    private Button avoidBnt;

    private TextView lengthTex;

    private boolean isStart = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_avoid, null);

        initView(view);

        getBlueToothData(new MainActivity.GetBlueToothData() {
            @Override
            public void onData(String data) {
                if (data.contains(OrderCode.TOGET_LENGTH)) {
                    //String length=data.substring(,OrderCode.TOGET_LENGTH.length());
                    lengthTex.setText("");
                }
            }
        });

        return view;
    }

    private void initView(View view) {

        avoidBnt = view.findViewById(R.id.avoid);

        avoidBnt.setOnClickListener(this);

        lengthTex = view.findViewById(R.id.length);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.avoid:
                if (isStart) {
                    avoidBnt.setText("暂停");
                    getDataCenter().setAvoid(OrderCode.AVOID_START);
                    isStart = false;
                } else {
                    avoidBnt.setText("开始");
                    getDataCenter().setAvoid(OrderCode.AVOID_STOP);
                    isStart = true;
                }
                break;
        }
    }
}
