package car.bluetooth.com.bluetoothcar.xxxcar.activity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private TextView tvLeft;
    private DrawerLayout drawer;
    private Button scanBnt;
    private FragmentManager fm;
    private Fragment currentFragment;
    private SearchBleDialog searchBleDialog;

    private List<Fragment> fragments = new ArrayList<>();

    private GetBlueToothData getBlueToothData;

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


    }

    private void initView() {
        navView = findViewById(R.id.nav_view);
        tvLeft = findViewById(R.id.menu_left);
        tvLeft.setOnClickListener(this);
        drawer = findViewById(R.id.drawer);
        navView.setItemIconTintList(null);
        navView.setCheckedItem(R.id.nav_control);
        scanBnt = findViewById(R.id.scan);
        scanBnt.setOnClickListener(this);
        progressHelper = new ProgressHelper();
        connectStateTex = findViewById(R.id.connctState);
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
        }
    }

    //蓝牙4.0的UUID,其中0000ffe1-0000-1000-8000-00805f9b34fb是广州汇承信息科技有限公司08蓝牙模块的UUID
    public static String HEART_RATE_MEASUREMENT = "0000ffe1-0000-1000-8000-00805f9b34fb";
    //蓝牙地址
    private String mDeviceAddress;
    private String mDeviceName;
    //蓝牙信号值
    private String mRssi;
    //蓝牙service,负责后台的蓝牙服务
    private static BluetoothLeService mBluetoothLeService;
    //private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();
    //蓝牙特征值
    private static BluetoothGattCharacteristic target_chara = null;
    // private Handler mhandler = new Handler();

    public void connectBle(BluetoothDevice bluetoothDevice) {

        progressHelper.showProgressDialog(this, "连接设备中");
        //从意图获取显示的蓝牙信息
        mDeviceName = bluetoothDevice.getName();
        mDeviceAddress = bluetoothDevice.getAddress();
        /* 启动蓝牙service */
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
    }

    public void sendDataNow(String data) {
        byte[] buff = data.getBytes();
        int len = buff.length;
        int[] lens = dataSeparate(len);
        for (int i = 0; i < lens[0]; i++) {
            String str = new String(buff, 20 * i, 20);
            target_chara.setValue(str);//只能一次发送20字节，所以这里要分包发送
            //调用蓝牙服务的写特征值方法实现发送数据
            mBluetoothLeService.writeCharacteristic(target_chara);
        }
        if (lens[1] != 0) {
            String str = new String(buff, 20 * lens[0], lens[1]);
            target_chara.setValue(str);
            //调用蓝牙服务的写特征值方法实现发送数据
            mBluetoothLeService.writeCharacteristic(target_chara);

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
            if (!mBluetoothLeService.initialize()) {
                finish();
            }
            // Automatically connects to the device upon successful start-up
            // initialization.
            // 根据蓝牙地址，连接设备
            mBluetoothLeService.connect(mDeviceAddress);

            mBluetoothLeService.setGetBlueToothData(new BluetoothLeService.GetBlueToothData() {

                @Override
                public void onData(String data) {
                    if (getBlueToothData != null) {
                        getBlueToothData.onData(data);
                    }
                }

                @Override
                public void connectSuccess() {
                    isSendRun = true;
                    connectStateTex.setText("连接成功");
                    startSendData();
                    progressHelper.dismissProgressDialog();
                }

                @Override
                public void connectFailed() {
                    progressHelper.dismissProgressDialog();
                    connectStateTex.setText("未连接");
                    isSendRun = false;
                    Toast.makeText(MainActivity.this, "连接错误,请重试", Toast.LENGTH_LONG).show();
                    searchBleDialog.show();
                }

                @Override
                public void gattServicesDiscover() {
                  /*  //获取设备的所有蓝牙服务
                    displayGattServices(mBluetoothLeService
                            .getSupportedGattServices());*/
                }

                @Override
                public void onRssi(int rssi) {

                }
            });

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }

    };
/*
    private void displayGattServices(List<BluetoothGattService> gattServices) {

        if (gattServices == null)
            return;
        String uuid = null;
        String unknownServiceString = "unknown_service";
        String unknownCharaString = "unknown_characteristic";

        // 服务数据,可扩展下拉列表的第一级数据
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();

        // 特征数据（隶属于某一级服务下面的特征值集合）
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();

        // 部分层次，所有特征值集合
        mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {

            // 获取服务列表
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();

            // 查表，根据该uuid获取对应的服务名称。SampleGattAttributes这个表需要自定义。

            gattServiceData.add(currentServiceData);

            System.out.println("Service uuid:" + uuid);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();

            // 从当前循环所指向的服务中读取特征值列表
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService
                    .getCharacteristics();

            ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            // 对于当前循环所指向的服务中的每一个特征值
            for (final BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();

                if (gattCharacteristic.getUuid().toString()
                        .equals(HEART_RATE_MEASUREMENT)) {
                    // 测试读取当前Characteristic数据，会触发mOnDataAvailable.onCharacteristicRead()
                    mhandler.postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            mBluetoothLeService
                                    .readCharacteristic(gattCharacteristic);
                        }
                    }, 200);

                    // 接受Characteristic被写的通知,收到蓝牙模块的数据后会触发mOnDataAvailable.onCharacteristicWrite()
                    mBluetoothLeService.setCharacteristicNotification(
                            gattCharacteristic, true);
                    target_chara = gattCharacteristic;
                    // 设置数据内容
                    // 往蓝牙模块写入数据
                    // mBluetoothLeService.writeCharacteristic(gattCharacteristic);
                }
                List<BluetoothGattDescriptor> descriptors = gattCharacteristic
                        .getDescriptors();
                for (BluetoothGattDescriptor descriptor : descriptors) {
                    System.out.println("---descriptor UUID:"
                            + descriptor.getUuid());
                    // 获取特征值的描述
                    mBluetoothLeService.getCharacteristicDescriptor(descriptor);
                    // mBluetoothLeService.setCharacteristicNotification(gattCharacteristic,
                    // true);
                }

                gattCharacteristicGroupData.add(currentCharaData);
            }
            // 按先后顺序，分层次放入特征值集合中，只有特征值
            mGattCharacteristics.add(charas);
            // 构件第二级扩展列表（服务下面的特征值）
            gattCharacteristicData.add(gattCharacteristicGroupData);

        }

    }*/

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


}