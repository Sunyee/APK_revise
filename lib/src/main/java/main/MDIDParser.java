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
import util.Util;
import vo.Common;

public class MDIDParser {
    public static String apkPath = "";
    public static int mdid_start = 100000;
    public static int mdid_end = 100000;
    public static final String WINRAR_PATH;
    public static final String CONFIG_PATH;

    public MDIDParser() {
    }

    public static boolean unZIPconfig() {
        String cmd = null;
        if (Util.isWindowsOS()) {
            cmd = WINRAR_PATH + " e " + apkPath + " " + "assets\\YQgame_config.xml" + " " + CONFIG_PATH;
        } else {
            cmd = "unzip -C -j " + apkPath + " " + "assets\\YQgame_config.xml" + " " + CONFIG_PATH;
        }

        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            System.out.println(proc);
            if (proc.waitFor() != 0 && proc.exitValue() == 0) {
                return false;
            }
        } catch (Exception var2) {
            var2.printStackTrace();
            System.out.println("异常");
        }

        return true;
    }

    public static boolean fixApk() {
        String cmd = null;
        if (Util.isWindowsOS()) {
            cmd = WINRAR_PATH + " u " + apkPath + " " + "assets\\YQgame_config.xml";
        } else {
            cmd = "unzip -C -j " + apkPath + " " + "assets\\YQgame_config.xml";
        }

        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            System.out.println(proc);
            if (proc.waitFor() != 0 && proc.exitValue() == 0) {
                return false;
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }

        return true;
    }

    public static void main(String[] args) {
        new Util();
        Util.getLocalProperty();
        new Common();
        apkPath = args[0];
        int mdidstart = Integer.parseInt(args[1]);
        System.out.println("mdidstart = " + mdidstart);
        delet();
        if (unZIPconfig()) {
            if (args.length > 2) {
                int mdidend = Integer.parseInt(args[2]);
                System.out.println("mdidend = " + mdidend);
                if (mdidstart > mdidend) {
                    System.out.println("开始的mdid比结束的mdid大，程序结束");
                    return;
                }

                while(mdidstart <= mdidend) {
                    System.out.println("--------------------------------");
                    System.out.println("-----------" + mdid_start + "开始打包------------");
                    System.out.println("--------------------------------");
                    mdid_start = mdidstart;
                    fix();
                    fixApk();
                    apkSign();
                    ++mdidstart;
                }
            } else {
                System.out.println("--------------------------------");
                System.out.println("-----------" + mdid_start + "开始打包------------");
                System.out.println("--------------------------------");
                mdid_start = mdidstart;
                fix();
                fixApk();
                apkSign();
            }

            System.out.println("--------------------------------");
            System.out.println("----打包完成----");
            System.out.println("--------------------------------");
        }

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
            System.out.println(process);
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

    public static void delet() {
        File apktempFile = new File(CONFIG_PATH);
        if (apktempFile == null) {
            System.out.println("无删除文件");
        } else {
            FileUtil.delDir(apktempFile);
        }

    }

    public static void fix() {
        String _360Content = FileUtil.read(CONFIG_PATH + "YQgame_config.xml");
        Pattern p = Pattern.compile("<mdid>(.*?)</mdid>");
        Matcher m = p.matcher(_360Content);
        m.find();
        String QHOPENSDK_APPID = "<mdid>" + m.group(1) + "</mdid>";
        _360Content = _360Content.replaceAll(QHOPENSDK_APPID, "<mdid>" + mdid_start + "</mdid>");
        System.out.println("a：" + QHOPENSDK_APPID);
        System.out.println("b：" + _360Content);
        FileUtil.write(CONFIG_PATH + "YQgame_config.xml", _360Content);
    }

    public static void apkSign() {
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
            System.out.println(process);
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
        buffer.append(" jarsigner -keystore ").append(Common.SECRET_KEY_PATH).append(Common.keyname).append(" -storepass " + Common.storepass).append(" -keypass " + Common.keypass).append(" -signedjar " + resultPath).append(prefixName + "_signed.apk").append(" ").append(apkPath + " ").append(Common.key_alias).append("  -digestalg  SHA1 -sigalg MD5withRSA");
        System.out.println("--签名完整命令-->" + buffer.toString());

        try {
            Process process = Runtime.getRuntime().exec(buffer.toString());
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
        zipapk = resultPath + prefixName + "_" + mdid_start + ".apk";
        buffer.setLength(0);
        String zipalignCmd = Common.ZIPALIGN_PATH;
        if (zipalignCmd == null || "".equals(zipalignCmd)) {
            zipalignCmd = " zipalign ";
        }

        System.out.println("--优化命令-->" + zipalignCmd);
        buffer.append(zipalignCmd + "  4 " + signapk + " " + zipapk);

        try {
            Process process = Runtime.getRuntime().exec(buffer.toString());
            if (process.waitFor() != 0) {
                System.out.println("优化失败！！！");
            }
        } catch (IOException var12) {
            var12.printStackTrace();
        } catch (InterruptedException var13) {
            var13.printStackTrace();
        }

        copyApk(zipapk, mdid_start + "");
        File apktempFile = new File(zipapk);
        if (apktempFile == null) {
            System.out.println("无删除文件");
        } else {
            FileUtil.delDir(apktempFile);
        }

        System.out.println("--------------------------------");
        System.out.println("----" + zipapk + " 签名完成----");
        System.out.println("--------------------------------");
    }

    static {
        WINRAR_PATH = System.getProperty("user.dir") + File.separator + "WinRAR.exe";
        CONFIG_PATH = System.getProperty("user.dir") + File.separator + "assets" + File.separator;
    }
}
