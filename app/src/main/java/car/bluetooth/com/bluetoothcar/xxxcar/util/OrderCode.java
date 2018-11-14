package car.bluetooth.com.bluetoothcar.xxxcar.util;

public class OrderCode {

    /* 1、运动控制：FF 55 07 00 02 05 XX XX XX XX
     最后4个参数表示左右轮的速度。速度范围是-255到255
        public static String ACTION_RUN = "ff5504000233XX";
 2、LED彩灯：ff 55 09 00 02 08 07 02 00 XX XX XX
     最后3个参数表示LED彩灯颜色。范围是0到255
     public static String LIGHT_LED = "ff5504000233XX";
 /*  3、播放音源：ff 55 04 00 02 33 XX
    最后1个参数表示音源序号。范围是0到100
4、停止播放音源：ff 55 04 00 02 33 fe
    当音源切换等情况，需要立即停止正在播放的音源。*/
    public static String AUDIO_START = "ff5504000233XX";
    public static String AUDIO_STOP = "ff5504000233fe";
    /* 5、表情面板指令（暂定，后面再改）：ff 55 0a 00 02 29 01 01 00 07 02 48 69*/
    public static String FACE_START = "ff5504000233XX";
    public static String FACE_STOP = "ff5504000233fe";
    /*  6、巡线开始（暂定，后面再改）：FF 55 05 00 00 11 03 01
    巡线暂停（暂定，后面再改）：FF 55 05 00 00 11 03 00*/
    public static String FIND_START = "FF55050000110301";
    public static String FIND_STOP = "FF55050000110300";
    /*    7、避障开始（暂定，后面再改)：FF 55 05 00 00 01 02 01
        避障暂停（暂定，后面再改)：FF 55 05 00 00 01 02 00*/
    public static String AVOID_START = "FF55050000010201";
    public static String AVOID_STOP = "FF55050000010200";
    /* 向小车发我要读取前方障碍物距离指令（暂定，后面再改)：FF 55 04 04 01 01 02
     返回距离（暂定，后面再改)：FF 55 04 22 XX XX XX XX*/
    public static String TOGET_LENGTH = "FF55050000010201";
    public static String GET_LENGTH = "FF55050000010200";

}
