package car.bluetooth.com.bluetoothcar.xxxcar.dialog;

import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import car.bluetooth.com.bluetoothcar.xxxcar.activity.MainActivity;

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

    private int SCAN_TIME = 10000;

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
        this.setCancelable(false);
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

        findViewById(R.id.cancle).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan:
                if (scanState) {
                    stopScanBle();
                } else {
                    startScanBle();
                }
                break;
            case R.id.cancle:
                dismiss();
                break;
        }
    }

    private void startScanBle() {
        mBluetoothAdapter.startLeScan(mLeScanCallback);
        scanBtn.setText("停止查找");
        scanTex.setText("查找中……");
        mLeDevices.clear();
        listAdapter.notifyDataSetChanged();
        scanState = true;
    }

    private void stopScanBle() {
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
        scanBtn.setEnabled(false);
        scanTex.setText("停止中……");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scanBtn.setEnabled(true);
                scanBtn.setText("开始查找");
                scanTex.setText("点击按钮开始查找");
                scanState = false;
            }
        }, 3000);

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
        public View getView(final int i, View convertView, ViewGroup viewGroup) {

            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.listitem_bleitem, null);
                holder = new ViewHolder();

                holder.deviceAddress = convertView.findViewById(R.id.address);
                holder.deviceName = convertView.findViewById(R.id.name);
                holder.connectBnt = convertView.findViewById(R.id.connect);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final BluetoothDevice device = mLeDevices.get(i);
            holder.deviceAddress.setText(device.getAddress());
            holder.deviceName.setText(device.getName());
            holder.connectBnt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.connectBle(device);
                    stopScanBle();
                    dismiss();
                }
            });

            return convertView;
        }

        private class ViewHolder {
            TextView deviceAddress;
            TextView deviceName;
            Button connectBnt;
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

        //判断是否有权限
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //请求权限
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            //判断是否需要 向用户解释，为什么要申请该权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(context, "需要打开权限搜索BLE设备", Toast.LENGTH_SHORT).show();
            }
        }

        //判断是否有权限
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //请求权限
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            //判断是否需要 向用户解释，为什么要申请该权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_CONTACTS)) {
                Toast.makeText(context, "需要打开权限搜索BLE设备", Toast.LENGTH_SHORT).show();
            }
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
