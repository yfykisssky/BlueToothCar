package car.bluetooth.com.bluetoothcar.xxxcar.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import car.bluetooth.com.bluetoothcar.xxxcar.activity.MainActivity;

public class BaseFragment extends Fragment {

    private MainActivity mainActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) this.getActivity();

    }

    public void getBlueToothData(MainActivity.GetBlueToothData getBlueToothData) {
        mainActivity.setGetBlueToothData(getBlueToothData);
    }

    public void sendData(String data) {
        mainActivity.sendData(data);
    }

}
