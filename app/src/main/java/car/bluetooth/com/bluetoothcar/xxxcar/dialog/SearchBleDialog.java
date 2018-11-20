package car.bluetooth.com.bluetoothcar.xxxcar.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import car.bluetooth.com.bluetoothcar.xxxcar.R;
import car.bluetooth.com.bluetoothcar.xxxcar.activity.MainActivity;
import car.bluetooth.com.bluetoothcar.xxxcar.view.ProgressHelper;

public class SearchBleDialog extends BaseDialog implements View.OnClickListener {

    private ListView searchList;
    private ArrayList<BluetoothDevice> mLeDevices = new ArrayList<>();
    private MainActivity activity;
    private Context context;

    // 扫描蓝牙按钮
    private Button scanBtn;
    private TextView scanTex;
    private boolean scanState = false;
    // 蓝牙适配器
    private BluetoothAdapter mBluetoothAdapter;
    private LeDeviceListAdapter listAdapter;

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

    public SearchBleDialog(@NonNull MainActivity context) {
        super(context);
        this.activity = context;
        this.context = context;
        initView();
        init_ble();
    }

    private void initView() {

        scanTex = findViewById(R.id.scanstable);
        searchList = findViewById(R.id.searchble);
        listAdapter = new LeDeviceListAdapter();
        searchList.setAdapter(listAdapter);
        scanBtn = findViewById(R.id.scan);
        scanBtn.setOnClickListener(this);

        searchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                activity.connectBle(mLeDevices.get(i));
                dismiss();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan:
                if (scanState) {
                    mBluetoothAdapter.startLeScan(mLeScanCallback);
                    scanBtn.setText("开始查找");
                    scanTex.setText("点击按钮开始查找");
                    scanState = false;
                } else {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    scanBtn.setText("停止查找");
                    scanTex.setText("查找中……");
                    scanState = true;
                }
                break;
        }
    }

    private class LeDeviceListAdapter extends BaseAdapter {

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

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {

            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.listitem_bleitem, null);
                holder = new ViewHolder();

                holder.deviceAddress = convertView.findViewById(R.id.address);
                holder.deviceName = convertView.findViewById(R.id.name);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            holder.deviceAddress.setText(device.getAddress());
            holder.deviceName.setText(device.getName());

            return convertView;
        }

        private class ViewHolder {
            TextView deviceAddress;
            TextView deviceName;
        }

    }

    private void init_ble() {
        // 手机硬件支持蓝牙
        if (!context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(context, "手机不支持BLE", Toast.LENGTH_SHORT).show();
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
            activity.startActivity(enableBtIntent);
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
