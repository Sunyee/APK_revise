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

public class ChangeAPPIDParser {
    public static String apkPath = "";
    public static int mdid_start = 100000;
    public static int mdid_end = 100000;
    public static String appid;
    public static String startTime = "";
    public static String WINRAR_PATH;
    public static String zipalign;
    public static String jarsigner;

    public static final String CONFIG_PATH;

    public ChangeAPPIDParser() {
    }

    public static void getLocalProperty() {
        String parent = new File(System.getProperty("user.dir")).getParent();
        WINRAR_PATH = parent + "\\WinRAR\\WinRAR.exe";
        zipalign = System.getProperty("user.dir") + "\\toolkit\\zipalign.exe";
        jarsigner = parent + "\\JDK\\jdk\\bin\\jarsigner.exe";
    }

    public static void fixGameConfig() {
        System.out.println("---------------91wan_config替换APPID开始---------------");
        String Content = FileUtil.read(CONFIG_PATH + "91wan_config.xml");
        Pattern p = Pattern.compile("<appid>(.*?)</appid>");
        Matcher m = p.matcher(Content);
        m.find();
        String SDK_APPID = "<appid>" + m.group(1) + "</appid>";
        Content = Content.replaceAll(SDK_APPID, "<appid>" + appid + "</appid>");
        System.out.println("替换前的mdid：" + SDK_APPID);
        System.out.println("替换mdid后的YQgame_config：" + Content);
        FileUtil.write(CONFIG_PATH + "91wan_config.xml", Content);
    }

    public static boolean zIPconfig() {
        String cmd = WINRAR_PATH + " u " + apkPath + " " + "assets\\91wan_config.xml";

        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            System.out.println(cmd);
            if (proc.waitFor() != 0 && proc.exitValue() == 0) {
                return false;
            }
        } catch (Exception var2) {
            var2.printStackTrace();
            System.out.println("异常");
        }

        return true;
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

    public static boolean unZIPconfig() {
        String cmd = null;
        if (Util.isWindowsOS()) {
            cmd = WINRAR_PATH + " e " + apkPath + " " + "assets\\91wan_config.xml" + " " + CONFIG_PATH;
        } else {
            cmd = "unzip -C -j " + apkPath + " " + "assets\\91wan_config.xml" + " " + CONFIG_PATH;
        }

        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            System.out.println(cmd);
            if (proc.waitFor() != 0 && proc.exitValue() == 0) {
                return false;
            }
        } catch (Exception var2) {
            var2.printStackTrace();
            System.out.println("异常");
        }

        return true;
    }

    public static void main(String[] args) {
        apkPath = args[0] + args[1];
        appid = args[2];
        delet(CONFIG_PATH + "91wan_config.xml");
        getLocalProperty();
        unZIPconfig();
        fixGameConfig();
        zIPconfig();
        apkSign();
    }

    public static void copyApk(String zipapk, String apk_path) {
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
            System.out.println("复制文件的路径为:" + apk_path);
            Process process = Runtime.getRuntime().exec(buffer.toString());
            System.out.println(buffer.toString());
            if (process.waitFor() != 0) {
                System.out.println("复制失败！！！");
            }
        } catch (InterruptedException var6) {
            var6.printStackTrace();
            System.out.println("1复制失败！！！");
        } catch (IOException var7) {
            var7.printStackTrace();
            System.out.println("2复制失败！！！");
        }

    }

    public static void apkSign() {
        System.out.println("---------------MDID替换完成，签名开始---------------");
        File srcFile = new File(apkPath);
        String parentPath = srcFile.getParent();
        String fileName = srcFile.getName();
        String prefixName = fileName.substring(0, fileName.lastIndexOf("."));
        String delcmd;
        if (Util.isWindowsOS()) {
            delcmd = WINRAR_PATH + " -ibck -inul d " + apkPath + "  " + "META-INF";
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

        buffer.append(jarsigner).append(" -keystore ").append(System.getProperty("user.dir") + File.separator + "toolkit" + File.separator + "keystore" + File.separator).append("fengteng.keystore").append(" -storepass fengteng").append(" -keypass fengteng").append(" -signedjar " + resultPath).append(prefixName + "_signed.apk").append(" ").append(apkPath + " ").append("ftgame").append("  -digestalg  SHA1 -sigalg MD5withRSA");
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
        zipapk = resultPath + prefixName + ".apk";
        buffer.setLength(0);
        String zipalignCmd = zipalign;
        if (zipalignCmd == null || "".equals(zipalignCmd)) {
            zipalignCmd = " zipalign ";
        }

        System.out.println("--优化命令-->" + zipalignCmd);
        buffer.append(zipalignCmd + "  4 " + signapk + " " + signapk);
        System.out.println("--优化命令-->zipalignCmd == " + buffer.toString());

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

    static {
        CONFIG_PATH = System.getProperty("user.dir") + File.separator + "assets" + File.separator;
    }
}
