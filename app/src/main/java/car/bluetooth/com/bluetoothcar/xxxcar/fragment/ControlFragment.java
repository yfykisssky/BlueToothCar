package car.bluetooth.com.bluetoothcar.xxxcar.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.larswerkman.holocolorpicker.ColorPicker;

import car.bluetooth.com.bluetoothcar.xxxcar.R;
import car.bluetooth.com.bluetoothcar.xxxcar.util.ViewTool;
import car.bluetooth.com.bluetoothcar.xxxcar.view.AnimView;
import car.bluetooth.com.bluetoothcar.xxxcar.view.ColorPickerView;
import car.bluetooth.com.bluetoothcar.xxxcar.view.MyRadioGroup;
import car.bluetooth.com.bluetoothcar.xxxcar.view.RockerView;

public class ControlFragment extends BaseFragment implements View.OnClickListener {

    private int speedScale;

    private ProgressBar speedProgress;

    private TextView colorTex;

    private LinearLayout colorLay;

    private LinearLayout soundLay;

    private LinearLayout faceLay;

    private MyRadioGroup faceRad;

    private RadioGroup soundRad;

    private ColorPickerView cpv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_control, null);

        initView(view);

        return view;
    }

    private void initView(View view) {

        speedProgress = view.findViewById(R.id.speed_progress);

        colorLay = view.findViewById(R.id.layout_color);
        soundLay = view.findViewById(R.id.layout_sound);
        faceLay = view.findViewById(R.id.layout_face);

        view.findViewById(R.id.color).setOnClickListener(this);
        view.findViewById(R.id.sound).setOnClickListener(this);
        view.findViewById(R.id.face).setOnClickListener(this);

        view.findViewById(R.id.color_cancle).setOnClickListener(this);
        colorTex = view.findViewById(R.id.color_show);

        colorTex.setBackgroundColor(Color.WHITE);

        cpv = view.findViewById(R.id.picker);
        cpv.setOnColorChangedListenner(new ColorPickerView.OnColorChangedListener() {
            @Override
            public void onColorChanged(int r, int g, int b) {
                if (r == 0 && g == 0 && b == 0) {
                    return;
                }
            }

            @Override
            public void onMoveColor(int r, int g, int b) {
                if (r == 0 && g == 0 && b == 0) {
                    return;
                }
                colorTex.setBackgroundColor(Color.rgb(r, g, b));
            }
        });

        initFaceRadio(view);

        faceRad = view.findViewById(R.id.face_radio);

        faceRad.setOnCheckedChangeListener(new MyRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MyRadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.face1:
                        clearFaceA();
                        animView1.setVisibility(View.VISIBLE);
                        break;
                    case R.id.face2:
                        clearFaceA();
                        animView2.setVisibility(View.VISIBLE);
                        break;
                    case R.id.face3:
                        clearFaceA();
                        animView3.setVisibility(View.VISIBLE);
                        break;
                    case R.id.face4:
                        clearFaceA();
                        animView4.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

       /* faceRad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

            }
        });*/

        soundRad = view.findViewById(R.id.sound_radio);

        soundRad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.sound1:
                        break;
                    case R.id.sound2:
                        break;
                    case R.id.sound3:
                        break;
                    case R.id.sound4:
                        break;
                }
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

    private void toSpeed(double angle) {
      /*  double leftWheelSpeed = 0;
        double leftRightSpeed = 0;
        if (angle >= 0 || angle <= 90) {
            leftWheelSpeed = angle / 90 * speedScale * 255;
            leftRightSpeed = (90 - angle) / 90 * speedScale * 255;
        } else if (angle <= 180 || angle > 90) {
            leftWheelSpeed = (180 - angle) / 180 * speedScale * 255;
            leftRightSpeed = angle / 180 * speedScale * 255;
        }*/

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.color:
                colorLay.setVisibility(View.VISIBLE);
                soundLay.setVisibility(View.GONE);
                faceLay.setVisibility(View.GONE);
                break;
            case R.id.sound:
                colorLay.setVisibility(View.GONE);
                soundLay.setVisibility(View.VISIBLE);
                faceLay.setVisibility(View.GONE);
                break;
            case R.id.face:
                colorLay.setVisibility(View.GONE);
                soundLay.setVisibility(View.GONE);
                faceLay.setVisibility(View.VISIBLE);
                break;
            case R.id.color_cancle:
                cpv.backToCenter();
                colorTex.setBackgroundColor(Color.WHITE);
                break;
        }
    }

    private RadioButton radioButtonF1;
    private RadioButton radioButtonF2;
    private RadioButton radioButtonF3;
    private RadioButton radioButtonF4;

    private AnimView animView1;
    private AnimView animView2;
    private AnimView animView3;
    private AnimView animView4;

    private void initFaceRadio(View view) {
        radioButtonF1 = view.findViewById(R.id.face1);
        ViewTool.setRadioButtonRect(radioButtonF1, getContext());
        radioButtonF2 = view.findViewById(R.id.face2);
        ViewTool.setRadioButtonRect(radioButtonF2, getContext());
        radioButtonF3 = view.findViewById(R.id.face3);
        ViewTool.setRadioButtonRect(radioButtonF3, getContext());
        radioButtonF4 = view.findViewById(R.id.face4);
        ViewTool.setRadioButtonRect(radioButtonF4, getContext());

        animView1 = view.findViewById(R.id.facea1);
        animView2 = view.findViewById(R.id.facea2);
        animView3 = view.findViewById(R.id.facea3);
        animView4 = view.findViewById(R.id.facea4);
    }

    private void clearFaceA() {
        animView1.setVisibility(View.GONE);
        animView2.setVisibility(View.GONE);
        animView3.setVisibility(View.GONE);
        animView4.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        faceRad.clearCheck();
    }
}
