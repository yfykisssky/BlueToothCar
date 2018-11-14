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

import car.bluetooth.com.bluetoothcar.xxxcar.R;
import car.bluetooth.com.bluetoothcar.xxxcar.view.RockerView;

public class AvoidFragment extends BaseFragment implements View.OnClickListener {

    private Button avoidBnt;

    private boolean isFind = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_avoid, null);

        initView(view);

        return view;
    }

    private void initView(View view) {

        avoidBnt = view.findViewById(R.id.avoid);

        avoidBnt.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.find:
                if (isFind) {
                    avoidBnt.setText("暂停");
                    isFind = false;
                } else {
                    avoidBnt.setText("开始");
                    isFind = true;
                }
                break;
        }
    }
}
