//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.CommonUtils;
import util.LogUtil;

public class CheckPackConfig {
    private static String aaptPath = System.getProperty("user.dir") + System.getProperty("file.separator") + "tools" + System.getProperty("file.separator") + "aapt";
    private static String logPath = System.getProperty("user.dir") + System.getProperty("file.separator") + "logs" + System.getProperty("file.separator") + getDayDate();
    public static String apkPath;
    public static String logFile;
    public static int dpermission = 0;

    public CheckPackConfig() {
    }

    public static void main(String[] args) {
        LogUtil.showLog("args =====" + args[0]);
        if (CommonUtils.isWindowsOS()) {
            apkPath = args[0];
            aaptPath = System.getProperty("user.dir") + System.getProperty("file.separator") + "tools" + System.getProperty("file.separator") + "aapt.exe";
        } else {
            apkPath = args[0];
        }

        File apk = new File(apkPath);
        logFile = logPath + System.getProperty("file.separator") + apk.getName().replace(".apk", "") + "_" + getDate() + ".txt";
        File day = new File(logPath);
        if (!day.exists()) {
            day.mkdirs();
        }

        File file = new File(logFile);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException var6) {
                var6.printStackTrace();
            }
        }

        try {
            PrintStream ps = new PrintStream(logFile);
            System.setOut(ps);
            getGamePackageName(apkPath);
            System.out.println("\n");
            getGameMD5(apkPath);
            System.out.println("\n");
            getGamePackagePermission(apkPath);
            System.out.println("\n");
            getGamePackageTargetSDKversion(apkPath);
            System.out.println("\n");
            ps.close();
            openFile(logFile);
        } catch (FileNotFoundException var5) {
            var5.printStackTrace();
        }

    }

    public static String getGamePackageName(String apkPath) {
        LogUtil.showLog("-----------------获取游戏游戏包基本信息---------------------");
        String line = "";
        String gamePackageName = "";
        StringBuffer buffer = new StringBuffer();
        buffer.setLength(0);
        String aaptCmd = aaptPath;
        if (aaptCmd == null || "".equals(aaptCmd)) {
            aaptCmd = " aapt ";
        }

        buffer.append(aaptCmd + " d badging ").append(apkPath).append(" | findstr \"package\" ");

        try {
            Process process = Runtime.getRuntime().exec(buffer.toString());
            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            Pattern p = Pattern.compile("package: name='(.*?)'");
            Pattern vcode = Pattern.compile("versionCode='(.*?)'");
            Pattern vname = Pattern.compile("versionName='(.*?)'");
            Pattern main = Pattern.compile("launchable-activity: name='(.*?)'");

            while((line = br.readLine()) != null) {
                Matcher match = p.matcher(line);
                Matcher match2 = vcode.matcher(line);
                Matcher match3 = vname.matcher(line);
                Matcher match4 = main.matcher(line);
                if (match.find()) {
                    gamePackageName = match.group(1);
                    LogUtil.showLog("--> 游戏包的包名 : " + gamePackageName + " <--");
                }

                if (match2.find()) {
                    LogUtil.showLog("--> 游戏包版本号 versionCode : " + match2.group(1) + " <--");
                }

                if (match3.find()) {
                    LogUtil.showLog("--> 游戏包版本名 versionName : " + match3.group(1) + " <--");
                }

                if (match4.find()) {
                    LogUtil.showLog("--> 游戏包launch-activity : " + match4.group(1) + " <--");
                }

                if (line.startsWith("native-code:")) {
                    LogUtil.showLog("--> 游戏包处理器架构so库 : " + line.replace("native-code:", "") + " <--");
                }
            }
        } catch (IOException var16) {
            var16.printStackTrace();
        } catch (Exception var17) {
            var17.printStackTrace();
        }

        if (null == gamePackageName || "".equals(gamePackageName)) {
            LogUtil.showLog("-->获取失败，请检查apk名或者路径是否有中文！<--");
        }

        return gamePackageName;
    }

    public static String getGamePackagePermission(String apkPath) {
        LogUtil.showLog("-----------------获取游戏包权限---------------------");
        LogUtil.showLog("-说明：《普通权限》为应用正常使用的权限，无需通知用户是否允许启用该权限，默认启用，风险较小-");
        LogUtil.showLog("-说明：《危险权限》为涉及用户隐私信息的数据或资源，需要通知用户是否允许启用该权限，存在一定风险-");
        LogUtil.showLog("-----------------------------------------------------------");
        LogUtil.showLog("-------------------以下为游戏包的权限申请-------------------");
        String line = "";
        String permisssion = "";
        StringBuffer buffer = new StringBuffer();
        buffer.setLength(0);
        String aaptCmd = aaptPath;
        if (aaptCmd == null || "".equals(aaptCmd)) {
            aaptCmd = " aapt ";
        }

        buffer.append(aaptCmd + " d permissions ").append(apkPath);

        try {
            Process process = Runtime.getRuntime().exec(buffer.toString());
            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            Pattern p = Pattern.compile("uses-permission: name='(.*?)'");

            while((line = br.readLine()) != null) {
                Matcher match = p.matcher(line);
                if (match.find()) {
                    permisssion = match.group(1);
                    LogUtil.showLog(switchGroup(permisssion));
                }
            }

            LogUtil.showLog("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            LogUtil.showLog("!!!!危险权限注意一共有【" + dpermission + "】个,请注意是否有用到!!!!");
            LogUtil.showLog("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        } catch (IOException var10) {
            var10.printStackTrace();
        } catch (Exception var11) {
            var11.printStackTrace();
        }

        return permisssion;
    }

    public static String getGamePackageTargetSDKversion(String apkPath) {
        LogUtil.showLog("-----------------获取游戏包targetSdkVersion---------------------");
        String line = "";
        String targetSdkVersion = "";
        StringBuffer buffer = new StringBuffer();
        buffer.setLength(0);
        String aaptCmd = aaptPath;
        if (aaptCmd == null || "".equals(aaptCmd)) {
            aaptCmd = " aapt ";
        }

        buffer.append(aaptCmd + " d badging ").append(apkPath).append(" | findstr \"targetSdkVersion\" ");

        try {
            Process process = Runtime.getRuntime().exec(buffer.toString());
            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            BufferedReader br = new BufferedReader(isr);
            Pattern p = Pattern.compile("targetSdkVersion:'(.*?)'");

            while((line = br.readLine()) != null) {
                Matcher match = p.matcher(line);
                if (match.find()) {
                    targetSdkVersion = match.group(1);
                    LogUtil.showLog("--> 游戏包的目标安卓版本号tagetSdkVerion = " + targetSdkVersion + " <--");
                    if (Integer.parseInt(targetSdkVersion) < 30) {
                        LogUtil.showLog("-->！注意： Google Play需要游戏包的tagetSdkVerion >= 30, 目前游戏包不符合Google Play上架标准 ");
                    }
                    break;
                }
            }
        } catch (IOException var10) {
            var10.printStackTrace();
        } catch (Exception var11) {
            var11.printStackTrace();
        }

        return targetSdkVersion;
    }

    public static void getGameMD5(String apkPath) {
        LogUtil.showLog("-----------------获取游戏包签名指纹信息---------------------");
        String line = "";
        StringBuffer buffer = new StringBuffer();
        buffer.setLength(0);
        buffer.append("keytool -printcert -jarfile ").append(apkPath);

        try {
            Process process = Runtime.getRuntime().exec(buffer.toString());
            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            BufferedReader br = new BufferedReader(isr);

            while((line = br.readLine()) != null) {
                if (line.startsWith("不是已签名的 jar 文件")) {
                    LogUtil.showLog("-->注意！游戏包未签名，无法安装，请检查签名文件");
                    LogUtil.showLog("-->注意！游戏包未签名，无法安装，请检查签名文件");
                    LogUtil.showLog("-->注意！游戏包未签名，无法安装，请检查签名文件");
                }

                String str;
                if (line.startsWith("\t MD5:")) {
                    str = line.replace("\t MD5: ", "");
                    LogUtil.showLog("--> 游戏包签名MD5 = " + str.replace(":", "").toLowerCase() + " <--");
                }

                if (line.startsWith("\t SHA1: ")) {
                    str = line.replace("\t SHA1: ", "");
                    LogUtil.showLog("--> 游戏包签名SHA1 = " + str + " <--");
                }

                if (line.startsWith("\t SHA256: ")) {
                    str = line.replace("\t SHA256: ", "");
                    LogUtil.showLog("--> 游戏包签名SHA256 = " + str + " <--");
                }
            }
        } catch (IOException var7) {
            var7.printStackTrace();
        } catch (Exception var8) {
            var8.printStackTrace();
        }

    }

    public static String getDate() {
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd-HH时mm分ss秒");
        String date = formater.format(new Date());
        return date;
    }

    public static String getDayDate() {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        String date = formater.format(new Date());
        return date;
    }

    public static void openFile(String filePath) {
        PrintStream out = System.out;
        System.setOut(out);
        System.out.println("检测运行完毕");

        try {
            if (CommonUtils.isWindowsOS()) {
                StringBuffer buffer = new StringBuffer();
                buffer.setLength(0);
                buffer.append("cmd.exe /c start ").append(filePath);
                Process process = Runtime.getRuntime().exec(buffer.toString());
                LogUtil.printStream(process);
                if (process.waitFor() != 0) {
                    LogUtil.showLog(new Date() + "打开文件失败");
                    return;
                }
            } else {
                String cmd = "open " + filePath;
                String[] command = new String[]{"/bin/sh", "-c", cmd};
                Process process = Runtime.getRuntime().exec(command);
                LogUtil.printStream(process);
                if (process.waitFor() != 0) {
                    LogUtil.showLog(new Date() + "打开文件失败");
                    return;
                }
            }
        } catch (IOException var5) {
            var5.printStackTrace();
        } catch (InterruptedException var6) {
            var6.printStackTrace();
        }

    }

    private static String switchGroup(String permission) {
        byte var2 = -1;
        switch(permission.hashCode()) {
        case -2062386608:
            if (permission.equals("android.permission.READ_SMS")) {
                var2 = 23;
            }
            break;
        case -1928411001:
            if (permission.equals("android.permission.READ_CALENDAR")) {
                var2 = 0;
            }
            break;
        case -1921431796:
            if (permission.equals("android.permission.READ_CALL_LOG")) {
                var2 = 12;
            }
            break;
        case -1888586689:
            if (permission.equals("android.permission.ACCESS_FINE_LOCATION")) {
                var2 = 6;
            }
            break;
        case -1813079487:
            if (permission.equals("android.permission.MANAGE_EXTERNAL_STORAGE")) {
                var2 = 28;
            }
            break;
        case -1674700861:
            if (permission.equals("android.permission.ANSWER_PHONE_CALLS")) {
                var2 = 19;
            }
            break;
        case -1479758289:
            if (permission.equals("android.permission.RECEIVE_WAP_PUSH")) {
                var2 = 24;
            }
            break;
        case -1238066820:
            if (permission.equals("android.permission.BODY_SENSORS")) {
                var2 = 20;
            }
            break;
        case -1164582768:
            if (permission.equals("android.permission.READ_PHONE_NUMBERS")) {
                var2 = 17;
            }
            break;
        case -895679497:
            if (permission.equals("android.permission.RECEIVE_MMS")) {
                var2 = 25;
            }
            break;
        case -895673731:
            if (permission.equals("android.permission.RECEIVE_SMS")) {
                var2 = 22;
            }
            break;
        case -406040016:
            if (permission.equals("android.permission.READ_EXTERNAL_STORAGE")) {
                var2 = 26;
            }
            break;
        case -63024214:
            if (permission.equals("android.permission.ACCESS_COARSE_LOCATION")) {
                var2 = 7;
            }
            break;
        case -5573545:
            if (permission.equals("android.permission.READ_PHONE_STATE")) {
                var2 = 10;
            }
            break;
        case 52602690:
            if (permission.equals("android.permission.SEND_SMS")) {
                var2 = 21;
            }
            break;
        case 112197485:
            if (permission.equals("android.permission.CALL_PHONE")) {
                var2 = 11;
            }
            break;
        case 214526995:
            if (permission.equals("android.permission.WRITE_CONTACTS")) {
                var2 = 4;
            }
            break;
        case 441496538:
            if (permission.equals("android.permission.ACCEPT_HANDOVER")) {
                var2 = 18;
            }
            break;
        case 463403621:
            if (permission.equals("android.permission.CAMERA")) {
                var2 = 2;
            }
            break;
        case 603653886:
            if (permission.equals("android.permission.WRITE_CALENDAR")) {
                var2 = 1;
            }
            break;
        case 610633091:
            if (permission.equals("android.permission.WRITE_CALL_LOG")) {
                var2 = 13;
            }
            break;
        case 784519842:
            if (permission.equals("android.permission.USE_SIP")) {
                var2 = 15;
            }
            break;
        case 952819282:
            if (permission.equals("android.permission.PROCESS_OUTGOING_CALLS")) {
                var2 = 16;
            }
            break;
        case 1271781903:
            if (permission.equals("android.permission.GET_ACCOUNTS")) {
                var2 = 5;
            }
            break;
        case 1365911975:
            if (permission.equals("android.permission.WRITE_EXTERNAL_STORAGE")) {
                var2 = 27;
            }
            break;
        case 1777263169:
            if (permission.equals("android.permission.REQUEST_INSTALL_PACKAGES")) {
                var2 = 29;
            }
            break;
        case 1831139720:
            if (permission.equals("android.permission.RECORD_AUDIO")) {
                var2 = 9;
            }
            break;
        case 1897049163:
            if (permission.equals("android.permission.ADD_VOICEMAIL")) {
                var2 = 14;
            }
            break;
        case 1977429404:
            if (permission.equals("android.permission.READ_CONTACTS")) {
                var2 = 3;
            }
            break;
        case 2024715147:
            if (permission.equals("android.permission.ACCESS_BACKGROUND_LOCATION")) {
                var2 = 8;
            }
        }

        switch(var2) {
        case 0:
            ++dpermission;
            return "危险权限注意！ : android.permission.READ_CALENDAR 允许程序读取用户的日程信息";
        case 1:
            ++dpermission;
            return "危险权限注意！ : android.permission.WRITE_CALENDAR 允许程序写入日程，但不可读取";
        case 2:
            ++dpermission;
            return "危险权限注意！ : android.permission.CAMERA 允许程序访问摄像头进行拍照";
        case 3:
            ++dpermission;
            return "危险权限注意！ : android.permission.READ_CONTACTS 允许程序访问联系人通讯录信息";
        case 4:
            ++dpermission;
            return "危险权限注意！ : android.permission.WRITE_CONTACTS 允许程序写入联系人,但不可读取";
        case 5:
            ++dpermission;
            return "危险权限注意！ : android.permission.GET_ACCOUNTS 允许程序访问账户Gmail列表";
        case 6:
            ++dpermission;
            return "危险权限注意！ : android.permission.ACCESS_FINE_LOCATION 允许程序通过GPS芯片接收卫星的定位信息";
        case 7:
            ++dpermission;
            return "危险权限注意！ : android.permission.ACCESS_COARSE_LOCATION 允许程序通过WiFi或移动基站的方式获取用户错略的经纬度信息";
        case 8:
            ++dpermission;
            return "危险权限注意！ : android.permission.ACCESS_BACKGROUND_LOCATION 允许应用程序在后台访问位置";
        case 9:
            ++dpermission;
            return "危险权限注意！ : android.permission.RECORD_AUDIO 允许程序录制声音通过手机或耳机的麦克";
        case 10:
            ++dpermission;
            return "危险权限注意！ : android.permission.READ_PHONE_STATE 允许程序访问电话状态";
        case 11:
            ++dpermission;
            return "危险权限注意！ : android.permission.CALL_PHONE 允许程序从非系统拨号器里拨打电话";
        case 12:
            ++dpermission;
            return "危险权限注意！ : android.permission.READ_CALL_LOG 允许程序读取通话记录";
        case 13:
            ++dpermission;
            return "危险权限注意！ : android.permission.WRITE_CALL_LOG 允许程序写入（但是不能读）用户的联系人数据";
        case 14:
            ++dpermission;
            return "危险权限注意！ : android.permission.ADD_VOICEMAIL 允许程序添加语音邮件系统";
        case 15:
            ++dpermission;
            return "危险权限注意！ : android.permission.USE_SIP 允许程序使用SIP视频服务";
        case 16:
            ++dpermission;
            return "危险权限注意！ : android.permission.PROCESS_OUTGOING_CALLS 允许程序监视，修改或放弃播出电话";
        case 17:
            ++dpermission;
            return "危险权限注意！ : android.permission.READ_PHONE_NUMBERS 允许程序读取设备的电话号码";
        case 18:
            ++dpermission;
            return "危险权限注意！ : android.permission.ACCEPT_HANDOVER 允许呼叫应用继续在另一个应用中启动的呼叫";
        case 19:
            ++dpermission;
            return "危险权限注意！ : android.permission.ANSWER_PHONE_CALLS 允许程序接听来电";
        case 20:
            ++dpermission;
            return "危险权限注意！ : android.permission.BODY_SENSORS 允许应用程序访问用户用来测量身体内部情况的传感器数据，例如心率";
        case 21:
            ++dpermission;
            return "危险权限注意！ : android.permission.SEND_SMS 允许程序发送短信";
        case 22:
            ++dpermission;
            return "危险权限注意！ : android.permission.RECEIVE_SMS 允许程序接收短信";
        case 23:
            ++dpermission;
            return "危险权限注意！ : android.permission.READ_SMS 允许程序读取短信内容";
        case 24:
            ++dpermission;
            return "危险权限注意！ : android.permission.RECEIVE_WAP_PUSH 允许程序接收WAP PUSH信息";
        case 25:
            ++dpermission;
            return "危险权限注意！ : android.permission.RECEIVE_MMS 允许程序接收彩信";
        case 26:
            ++dpermission;
            return "危险权限注意！ : android.permission.READ_EXTERNAL_STORAGE 允许程序可以读取设备外部存储空间";
        case 27:
            ++dpermission;
            return "危险权限注意！ : android.permission.WRITE_EXTERNAL_STORAGE 允许程序写入外部存储,如SD卡上写文件";
        case 28:
            ++dpermission;
            return "特殊权限注意！！！ : android.permission.MANAGE_EXTERNAL_STORAGE 允许程序读写手机SD卡--游戏包tagetSdkVerion>=30时需要";
        case 29:
            ++dpermission;
            return "危险权限注意！ : android.permission.REQUEST_INSTALL_PACKAGES 允许程序内安装应用";
        default:
            return "普通权限 : " + permission;
        }
    }
}
