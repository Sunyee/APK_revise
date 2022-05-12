//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package main;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.FileUtil;
import util.LogUtil;
import util.Util;
import vo.BuilderController;
import vo.Common;
import vo.DecodeController;

public class ChangePackParser {
    public static String apkPath = "";
    public static String apkName = "";
    public static final String APKTEMP;
    public static final String dist;

    public ChangePackParser() {
    }

    public static void main(String[] args) {
        apkPath = args[0];
        apkName = args[1];
        delet(APKTEMP);
        Util.getLocalProperty();
        (new DecodeController(apkPath)).decode();
        fixName();
        (new BuilderController()).build(APKTEMP);
        File srcFile = new File(apkPath);
        String fileName = srcFile.getName();
        apkPath = dist + fileName;
        apkSign();
    }

    public static void fixName() {
        System.out.println("---------------替换AndroidManifest中的包名_" + apkName + "---------------");
        String Content = FileUtil.read(APKTEMP + "AndroidManifest.xml");
        Pattern p = Pattern.compile("android:label=\"(.*?)\"");
        Matcher m = p.matcher(Content);
        m.find();
        String QHOPENSDK_APPID = "android:label=\"" + m.group(1) + "\"";
        Content = Content.replaceAll(QHOPENSDK_APPID, "android:label=\"" + apkName + "\"");
        System.out.println("替换前的游戏名：" + QHOPENSDK_APPID);
        System.out.println("替换游戏名后的AndroidManifest：" + Content);
        FileUtil.write(APKTEMP + "AndroidManifest.xml", Content);
    }

    public static void apkSign() {
        System.out.println("---------------MDID替换完成，签名开始---------------");
        File srcFile = new File(apkPath);
        String parentPath = srcFile.getParent();
        String fileName = srcFile.getName();
        String prefixName = fileName.substring(0, fileName.lastIndexOf("."));
        String delcmd;
        if (Util.isWindowsOS()) {
            delcmd = Common.RAR_PATH + " -ibck -inul d " + apkPath + "  " + "META-INF";
        } else {
            delcmd = " zip  -d " + apkPath + "  " + "META-INF/*";
        }

        try {
            Process process = Runtime.getRuntime().exec(delcmd);
            System.out.println(delcmd);
            if (process.waitFor() != 0 && process.exitValue() == 0) {
                System.out.println("exit");
            }
        } catch (Exception var16) {
            var16.printStackTrace();
        }

        String resultPath = parentPath + File.separator;
        File resultFile = new File(resultPath);
        if (!resultFile.exists()) {
            resultFile.mkdir();
        }

        StringBuffer buffer = new StringBuffer();
        buffer.setLength(0);
        //liusy
        buffer.append(Common.JARSIGNER).append(" -keystore ").append(Common.SECRET_KEY_PATH).append(Common.keyname).append(" -storepass " + Common.storepass).append(" -keypass " + Common.keypass).append(" -signedjar " + resultPath).append(prefixName + "_signed.apk").append(" ").append(apkPath + " ").append(Common.key_alias).append("  -digestalg  SHA1 -sigalg MD5withRSA");
        System.out.println("--签名完整命令-->" + buffer.toString());

        try {
            Process process = Runtime.getRuntime().exec(buffer.toString());
            LogUtil.printStream(process);
            if (process.waitFor() != 0) {
                System.out.println("签名失败~~");
            }
        } catch (IOException var14) {
            var14.printStackTrace();
        } catch (InterruptedException var15) {
            var15.printStackTrace();
        }

        String signapk = resultPath + prefixName + "_signed.apk";
        String zipapk = "";
        zipapk = resultPath + apkName + ".apk";
        buffer.setLength(0);
        String zipalignCmd = Common.ZIPALIGN_PATH;
        if (zipalignCmd == null || "".equals(zipalignCmd)) {
            zipalignCmd = " zipalign ";
        }

        System.out.println("--优化命令-->" + zipalignCmd);
        buffer.append(zipalignCmd + "  4 " + signapk + " " + zipapk);

        try {
            Process process = Runtime.getRuntime().exec(buffer.toString());
            LogUtil.printStream(process);
            if (process.waitFor() != 0) {
                System.out.println("优化失败！！！");
            }
        } catch (IOException var12) {
            var12.printStackTrace();
        } catch (InterruptedException var13) {
            var13.printStackTrace();
        }

        copyApk(zipapk);
        File apktempFile = new File(zipapk);
        if (apktempFile == null) {
            System.out.println("无删除文件");
        } else {
            FileUtil.delDir(apktempFile);
        }

        System.out.println("----------------------------------------");
        System.out.println("--------" + zipapk + " 签名完成--------");
        System.out.println("----------------------------------------");
    }

    public static void copyApk(String zipapk) {
        try {
            StringBuffer buffer = new StringBuffer();
            String temp_path = System.getProperty("user.dir") + System.getProperty("file.separator") + "apk_file";
            File apk_file = new File(temp_path);
            if (!apk_file.exists()) {
                apk_file.mkdirs();
                System.out.println("创建apkfile目录成功");
            } else {
                System.out.println("apkfile目录已经存在" + apk_file);
            }

            if (Util.isWindowsOS()) {
                buffer.append(" cmd.exe /c copy ");
            } else {
                buffer.append(" cp -f -r ");
            }

            buffer.append(zipapk + " " + temp_path + File.separator);
            Process process = Runtime.getRuntime().exec(buffer.toString());
            System.out.println(buffer.toString());
            if (process.waitFor() != 0) {
                System.out.println("复制失败！！！");
            }
        } catch (InterruptedException var5) {
            var5.printStackTrace();
            System.out.println("1复制失败！！！");
        } catch (IOException var6) {
            var6.printStackTrace();
            System.out.println("2复制失败！！！");
        }

    }

    public static void delet(String path) {
        File apktempFile = new File(path);
        if (apktempFile == null) {
            System.out.println("无删除文件");
        } else {
            System.out.println("删除文件:" + path);
            FileUtil.delDir(apktempFile);
        }

    }

    public static void changePack() {
    }

    static {
        APKTEMP = Common.APK_TEMP + "smali_apktemp" + File.separator;
        dist = APKTEMP + "dist" + File.separator;
    }
}
