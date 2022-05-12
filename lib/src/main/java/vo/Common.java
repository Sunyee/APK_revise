//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package vo;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Common {
    public static String JAVA = "";
    public static String JARSIGNER = "";
    public static String JDK_BIN_PATH = "";
    public static String RAR_PATH = "";
    public static String ZIPALIGN_PATH = "";
    public static String keyname = "";
    public static String storepass = "";
    public static String keypass = "";
    public static String key_alias = "";
    public static final String SECRET_KEY_PATH;
    public static final String APK_TEMP;
    public static String CONFIG;
    public static final String SECRET_KEY_FOR_1_DOT_7 = "  -digestalg  SHA1 -sigalg MD5withRSA";
    public static final String KEY_NAME = "keyname";
    public static final String KEY_ALI = "key_alias";
    public static final String KEY_STOREPASS = "storepass";
    public static final String KEYPASS = "keypass";
    public static boolean isFix;
    public static Map<String, String> channlyqgame;
    public static Map<String, String> channlDcn;
    public static Map<String, String> channlDcn572;
    public static Map<String, String> channlDcn993;
    public static Map<String, String> channlDcn999;
    public static Map<String, String> channlHtc;
    public static Map<String, String> channlSina;
    public static Map<String, String> channlLingjing;
    public static Map<String, String> channlBjlw;
    public static Map<String, Map> channlKey;
    public static Object[] key;
    public static String sdkName;
    public static Map<String, String> valueMap;

    public Common() {
    }

    public static String getToolkitPath() {
        return System.getProperty("user.dir") + System.getProperty("file.separator") + "toolkit" + System.getProperty("file.separator");
    }

    static {
        SECRET_KEY_PATH = System.getProperty("user.dir") + File.separator + "keystore" + File.separator;
        APK_TEMP = System.getProperty("user.dir") + System.getProperty("file.separator") + "apktemp" + System.getProperty("file.separator");
        CONFIG = System.getProperty("user.dir") + File.separator + "local_config.properties";
        isFix = false;
        key = null;
        sdkName = "";
        valueMap = new HashMap();
        channlyqgame = new HashMap();
        channlyqgame.put("keyname", keyname);
        channlyqgame.put("storepass", storepass);
        channlyqgame.put("keypass", keypass);
        channlyqgame.put("key_alias", key_alias);
        channlDcn = new HashMap();
        channlDcn.put("keyname", "dcn_442.keystore");
        channlDcn.put("storepass", "downjoy_442");
        channlDcn.put("keypass", "downjoy_442");
        channlDcn.put("key_alias", "442");
        channlDcn572 = new HashMap();
        channlDcn572.put("keyname", "dcn_572.keystore");
        channlDcn572.put("storepass", "downjoy_572");
        channlDcn572.put("keypass", "downjoy_572");
        channlDcn572.put("key_alias", "572");
        channlDcn993 = new HashMap();
        channlDcn993.put("keyname", "dcn_993.keystore");
        channlDcn993.put("storepass", "downjoy_993");
        channlDcn993.put("keypass", "downjoy_993");
        channlDcn993.put("key_alias", "993");
        channlDcn999 = new HashMap();
        channlDcn999.put("keyname", "dcn_999.keystore");
        channlDcn999.put("storepass", "downjoy_999");
        channlDcn999.put("keypass", "downjoy_999");
        channlDcn999.put("key_alias", "999");
        channlHtc = new HashMap();
        channlHtc.put("keyname", "htc.keystore");
        channlHtc.put("storepass", "htc_37wan");
        channlHtc.put("keypass", "htc_37wan");
        channlHtc.put("key_alias", "htc.keystore");
        channlSina = new HashMap();
        channlSina.put("keyname", "sina.keystore");
        channlSina.put("storepass", "android");
        channlSina.put("keypass", "android");
        channlSina.put("key_alias", "androiddebugkey");
        channlLingjing = new HashMap();
        channlLingjing.put("keyname", "lingjing.keystore");
        channlLingjing.put("storepass", "guangzhou_sq");
        channlLingjing.put("keypass", "guangzhou_sq");
        channlLingjing.put("key_alias", "android.keystore");
        channlBjlw = new HashMap();
        channlBjlw.put("keyname", "beijinglewan.keystore");
        channlBjlw.put("storepass", "lewanduo");
        channlBjlw.put("keypass", "lewanduo");
        channlBjlw.put("key_alias", "lewanduo");
        channlKey = new HashMap();
        channlKey.put("yqgame", channlyqgame);
        channlKey.put("dcn", channlDcn);
        channlKey.put("dcn572", channlDcn572);
        channlKey.put("dcn999", channlDcn999);
        channlKey.put("dcn993", channlDcn993);
        channlKey.put("htc", channlHtc);
        channlKey.put("sina", channlSina);
        channlKey.put("lingjing", channlLingjing);
        channlKey.put("beijinglewan", channlBjlw);
    }
}
