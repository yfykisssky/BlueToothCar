package car.bluetooth.com.bluetoothcar.xxxcar.activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import car.bluetooth.com.bluetoothcar.xxxcar.R;
import car.bluetooth.com.bluetoothcar.xxxcar.dialog.SearchBleDialog;
import car.bluetooth.com.bluetoothcar.xxxcar.fragment.AvoidFragment;
import car.bluetooth.com.bluetoothcar.xxxcar.fragment.ControlFragment;
import car.bluetooth.com.bluetoothcar.xxxcar.fragment.FindFragment;
import car.bluetooth.com.bluetoothcar.xxxcar.fragment.GravityFragment;
import car.bluetooth.com.bluetoothcar.xxxcar.fragment.ShakeFragment;
import car.bluetooth.com.bluetoothcar.xxxcar.service.BluetoothLeService;
import car.bluetooth.com.bluetoothcar.xxxcar.util.DataOrderCenter;
import car.bluetooth.com.bluetoothcar.xxxcar.util.OrderCode;
import car.bluetooth.com.bluetoothcar.xxxcar.view.ProgressHelper;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private NavigationView navView;
    private LinearLayout tvLeft;
    private DrawerLayout drawer;
    private FragmentManager fm;
    private Fragment currentFragment;
    private SearchBleDialog searchBleDialog;

    private List<Fragment> fragments = new ArrayList<>();

    private GetBlueToothData getBlueToothData;

    private TextView menuChoiceTex;

    private boolean isSendRun = false;

    private final int ORDER_TIME = 60;

    public DataOrderCenter dataOrderCenter = new DataOrderCenter();

    private ProgressHelper progressHelper;

    private TextView connectStateTex;

    public void setGetBlueToothData(GetBlueToothData getBlueToothData) {
        this.getBlueToothData = getBlueToothData;
    }

    public interface GetBlueToothData {
        void onData(String data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        initView();

        //菜单的点击事件
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                menuChoiceTex.setText(item.getTitle());
                switch (item.getItemId()) {
                    case R.id.nav_control:
                        switchFragment(fragments.get(0));
                        break;
                    case R.id.nav_find:
                        switchFragment(fragments.get(1));
                        break;
                    case R.id.nav_avoid:
                        switchFragment(fragments.get(2));
                        break;
                    case R.id.nav_shake:
                        switchFragment(fragments.get(3));
                        break;
                    case R.id.nav_gravity:
                        switchFragment(fragments.get(4));
                        break;
                }
                return true;
            }
        });

        initFragment();

        initBleService();

    }

    private void initView() {
        navView = findViewById(R.id.nav_view);
        tvLeft = findViewById(R.id.menu_left);
        menuChoiceTex = findViewById(R.id.menu_choice);
        tvLeft.setOnClickListener(this);
        drawer = findViewById(R.id.drawer);
        navView.setItemIconTintList(null);
        navView.setCheckedItem(R.id.nav_control);
        findViewById(R.id.scan).setOnClickListener(this);
        progressHelper = new ProgressHelper();
        connectStateTex = findViewById(R.id.connctState);
        findViewById(R.id.help).setOnClickListener(this);

    }

    private void switchFragment(Fragment fragment) {
        if (fragment.isAdded()) {
            fm.beginTransaction().hide(currentFragment).show(fragment).commit();
        } else {
            fm.beginTransaction().add(R.id.frame_layout, fragment).hide(currentFragment).show(fragment).commit();
        }
        currentFragment = fragment;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    private void initFragment() {
        fm = getSupportFragmentManager();
        fragments.add(new ControlFragment());
        fragments.add(new FindFragment());
        fragments.add(new AvoidFragment());
        fragments.add(new ShakeFragment());
        fragments.add(new GravityFragment());
        currentFragment = fragments.get(0);
        fm.beginTransaction().add(R.id.frame_layout, currentFragment).commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_left:
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.scan:
                searchBleDialog = new SearchBleDialog(this);
                searchBleDialog.show();
                break;
            case R.id.help:
                startActivity(new Intent(this, HelpActivity.class));
                break;
        }
    }

    //蓝牙4.0的UUID,其中0000ffe1-0000-1000-8000-00805f9b34fb是广州汇承信息科技有限公司08蓝牙模块的UUID
    public static final String SERVICE_UUID = "0000ffe0-0000-1000-8000-00805f9b34fb";
    public static final String CHARAC_UUID = "0000ffe1-0000-1000-8000-00805f9b34fb";
    //蓝牙service,负责后台的蓝牙服务
    private BluetoothLeService mBluetoothLeService;
    //蓝牙写入
    private BluetoothGattCharacteristic targetDara = null;

    private void initBleService() {
        /* 启动蓝牙service */
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    public void connectBle(BluetoothDevice bluetoothDevice) {

        progressHelper.showProgressDialog(this, "连接设备中");
        // 根据蓝牙地址，连接设备
        mBluetoothLeService.setGetBlueToothData(new BluetoothLeService.GetBlueToothData() {

            @Override
            public void onData(String data) {
                Message msg = new Message();
                msg.what = 2;
                msg.obj = data;
                viewHandler.sendMessage(msg);
            }

            @Override
            public void connectSuccess() {

            }

            @Override
            public void connectFailed() {
                viewHandler.sendEmptyMessage(0);
            }

            @Override
            public void gattServicesDiscover(BluetoothGatt gatt) {
                Message msg = new Message();
                msg.what = 1;
                msg.obj = gatt;
                viewHandler.sendMessage(msg);
            }

            @Override
            public void onRssi(int rssi) {

            }
        });

        mBluetoothLeService.connect(bluetoothDevice);

    }


    @SuppressLint("HandlerLeak")
    private Handler viewHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    connectError();
                    break;
                case 1:
                    isSendRun = true;
                    connectStateTex.setText("连接成功");
                    BluetoothGatt gatt = (BluetoothGatt) msg.obj;
                    BluetoothGattService service = gatt.getService(UUID.fromString(SERVICE_UUID));
                    targetDara = service.getCharacteristic(UUID.fromString(CHARAC_UUID));
                    mBluetoothLeService.setCharacteristicNotification(targetDara, true);
                    startSendData();
                    progressHelper.dismissProgressDialog();
                    break;
                case 2:
                    String data = (String) msg.obj;
                    if (getBlueToothData != null) {
                        getBlueToothData.onData(data);
                    }
                    break;
            }
        }
    };

    private void connectError() {
        progressHelper.dismissProgressDialog();
        connectStateTex.setText("未连接");
        isSendRun = false;
        Toast.makeText(MainActivity.this, "连接错误,请重试", Toast.LENGTH_LONG).show();
        searchBleDialog.show();
    }

    public void sendDataNow(String data) {
        byte[] buff = data.getBytes();
        int len = buff.length;
        int[] lens = dataSeparate(len);
        for (int i = 0; i < lens[0]; i++) {
            String str = new String(buff, 20 * i, 20);
            targetDara.setValue(str);//只能一次发送20字节，所以这里要分包发送
            //调用蓝牙服务的写特征值方法实现发送数据
            mBluetoothLeService.writeCharacteristic(targetDara);
        }
        if (lens[1] != 0) {
            String str = new String(buff, 20 * lens[0], lens[1]);
            targetDara.setValue(str);
            //调用蓝牙服务的写特征值方法实现发送数据
            mBluetoothLeService.writeCharacteristic(targetDara);

        }
    }

    public int[] dataSeparate(int len) {
        int[] lens = new int[2];
        lens[0] = len / 20;
        lens[1] = len - 20 * lens[0];
        return lens;
    }

    /* BluetoothLeService绑定的回调函数 */
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName,
                                       IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service)
                    .getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            if (mBluetoothLeService != null) {
                mBluetoothLeService.close();
                mBluetoothLeService = null;
            }
        }

    };

    public void startSendData() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (isSendRun) {
                    try {

                        if (!TextUtils.isEmpty(dataOrderCenter.getAvoid())) {
                            sendDataNow(dataOrderCenter.getAvoid());
                            dataOrderCenter.setAvoid("");
                            Thread.sleep(ORDER_TIME);
                            sendDataNow(OrderCode.GET_LENGTH);
                            Thread.sleep(ORDER_TIME);
                        }

                        if (!TextUtils.isEmpty(dataOrderCenter.getFind())) {
                            sendDataNow(dataOrderCenter.getFind());
                            dataOrderCenter.setFind("");
                            Thread.sleep(ORDER_TIME);
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isSendRun = false;
        unbindService(mServiceConnection);
    }
}