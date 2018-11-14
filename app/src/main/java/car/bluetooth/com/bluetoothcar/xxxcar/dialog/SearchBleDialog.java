package car.bluetooth.com.bluetoothcar.xxxcar.dialog;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import car.bluetooth.com.bluetoothcar.xxxcar.R;

public class SearchBleDialog extends BaseDialog implements View.OnClickListener {

    private ListView searchList;
    private ArrayList<BluetoothDevice> mLeDevices = new ArrayList<>();
    private Context context;

    // 扫描蓝牙按钮
    private Button scanBtn;
    // 蓝牙适配器
    BluetoothAdapter mBluetoothAdapter;
    LeDeviceListAdapter listAdapter;
    /*  // 蓝牙信号强度
      private ArrayList<Integer> rssis;*/
    // 描述扫描蓝牙的状态
    private boolean mScanning;
    private boolean scan_flag;
    private Handler mHandler = new Handler();
    int REQUEST_ENABLE_BT = 1;
    // 蓝牙扫描时间
    private static final long SCAN_PERIOD = 10000;
    @SuppressLint("HandlerLeak")
    private Handler viewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BluetoothDevice device = (BluetoothDevice) msg.obj;
            mLeDevices.add(device);
            listAdapter.notifyDataSetChanged();
        }
    };

    @Override
    int getContentViewId() {
        return R.layout.dialog_searchble;
    }

    public SearchBleDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        initView();
        init_ble();
        scanLeDevice(true);
    }

    private void initView() {

        searchList = findViewById(R.id.searchble);
        listAdapter = new LeDeviceListAdapter();
        searchList.setAdapter(listAdapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        /*    case R.id.sendcode:

                timeHandler.post(timeRunnable);

                sendCodeBnt.setEnabled(false);
                sendCodeBnt.setText("重新发送(" + String.valueOf(intTime) + ")");
                try {
                    sendCode(phoneTex.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;*/
        }
    }

    private class LeDeviceListAdapter extends BaseAdapter {

        private LayoutInflater mInflator;

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = mInflator.inflate(R.layout.listitem_bleitem, null);
            TextView deviceAddress = view.findViewById(R.id.address);
            TextView deviceName = view.findViewById(R.id.name);

            BluetoothDevice device = mLeDevices.get(i);
            deviceAddress.setText(device.getAddress());
            deviceName.setText(device.getName());

            return view;
        }
    }

    private void init_ble() {
        // 手机硬件支持蓝牙
        if (!context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(context, "不支持BLE", Toast.LENGTH_SHORT).show();
            dismiss();
        }
        // Initializes Bluetooth adapter.
        // 获取手机本地的蓝牙适配器
        final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        // 打开蓝牙权限
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //context.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

    }

    //扫描蓝牙设备
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
        /*    mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    scan_flag = true;
                   *//* scan_btn.setText("扫描设备");
                    Log.i("SCAN", "stop.....................");*//*
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);*/
            /* 开始扫描蓝牙设备，带mLeScanCallback 回调函数 */
            Log.i("SCAN", "begin.....................");
            mScanning = true;
            scan_flag = false;
            //scan_btn.setText("停止扫描");
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            Log.i("Stop", "stoping................");
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
            scan_flag = true;
        }

    }

    //蓝牙扫描回调函数 实现扫描蓝牙设备，回调蓝牙BluetoothDevice，可以获取name MAC等信息
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi,
                             byte[] scanRecord) {

            Message msg = new Message();
            msg.obj = device;
            viewHandler.sendMessage(msg);

        }
    };

}
