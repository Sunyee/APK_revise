package util;

/* loaded from: appid.jar:util/CommonUtils.class */
public class CommonUtils {
    public static final int IS_UNKONOW_OS = 0;
    public static final int IS_WINDOWS_OS = 1;
    public static final int IS_MAC_OS = 2;
    public static final int IS_LINUX_OS = 3;

    public static boolean isWindowsOS() {
        String osName = System.getProperty("os.name");
        if (osName == null) {
            System.out.println("获取OS失败");
            return false;
        } else if (osName.contains("Windows")) {
            return true;
        } else {
            return false;
        }
    }

    public static int getOSName() {
        String osName = System.getProperty("os.name");
        if (osName == null) {
            System.out.println("获取OS失败");
            return 0;
        } else if (osName.contains("Windows") || osName.contains("windows")) {
            return 1;
        } else {
            if (osName.contains("Mac") || osName.contains("mac")) {
                return 2;
            }
            if (osName.contains("Linux") || osName.contains("linux")) {
                return 3;
            }
            return 0;
        }
    }
}