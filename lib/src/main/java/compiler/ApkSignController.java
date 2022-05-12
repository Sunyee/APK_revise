//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package compiler;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import util.Util;
import vo.CommandParamsParser;
import vo.Common;

public class ApkSignController {
    private static String sdkName;
    private static String sdkTag;

    public ApkSignController() {
    }

    public static void apkSign(String sourcefile) throws IOException {
        File srcFile = new File(sourcefile);
        String parentPath = srcFile.getParent();
        String fileName = srcFile.getName();
        String prefixName = fileName.substring(0, fileName.lastIndexOf("."));
        String delcmd;
        if (Util.isWindowsOS()) {
            delcmd = Common.RAR_PATH + " -ibck -inul d " + sourcefile + "  " + "META-INF";
        } else {
            delcmd = " zip  -d " + sourcefile + "  " + "META-INF";
        }

        try {
            Process proc = Runtime.getRuntime().exec(delcmd);
            if (proc.waitFor() != 0 && proc.exitValue() == 0) {
                System.out.println("exit");
            }
        } catch (Exception var17) {
            var17.printStackTrace();
        }

        File ff = new File(Common.JDK_BIN_PATH);
        String resultPath = parentPath + File.separator;
        File resultFile = new File(resultPath);
        if (!resultFile.exists()) {
            resultFile.mkdir();
        }

        StringBuffer buffer = new StringBuffer();
        if (!isFix()) {
            System.out.println("当前是yqgame的签名");
            sdkTag = "yqgame";
            System.out.println("keyname = " + Common.keyname);
            System.out.println("storepass = " + Common.storepass);
            System.out.println("keypass = " + Common.keypass);
            System.out.println("key_alias = " + Common.key_alias);
            buffer.setLength(0);
            buffer.append(" jarsigner -keystore ").append(Common.SECRET_KEY_PATH).append(Common.keyname).append(" -storepass " + Common.storepass).append(" -keypass " + Common.keypass).append(" -signedjar " + resultPath).append(prefixName + "_signed.apk").append(" ").append(sourcefile + " ").append(Common.key_alias).append("  -digestalg  SHA1 -sigalg MD5withRSA");
            System.out.println("签名：" + buffer.toString());
        } else {
            System.out.println("keyname = " + (String)Common.valueMap.get("keyname"));
            System.out.println("storepass = " + (String)Common.valueMap.get("storepass"));
            System.out.println("keypass = " + (String)Common.valueMap.get("keypass"));
            System.out.println("key_alias = " + (String)Common.valueMap.get("key_alias"));
            buffer.setLength(0);
            buffer.append(Common.JARSIGNER).append(" -keystore ").append(Common.SECRET_KEY_PATH).append((String)Common.valueMap.get("keyname")).append(" -storepass " + (String)Common.valueMap.get("storepass")).append(" -keypass " + (String)Common.valueMap.get("keypass")).append(" -signedjar " + resultPath).append(prefixName + "_signed.apk").append(" ").append(sourcefile + " ").append((String)Common.valueMap.get("key_alias")).append("  -digestalg  SHA1 -sigalg MD5withRSA");
            System.out.println("签名：" + buffer.toString());
        }

        try {
            Process process = Runtime.getRuntime().exec(buffer.toString(), (String[])null, ff);
            if (process.waitFor() != 0) {
                System.out.println("签名失败~~");
            }
        } catch (IOException var15) {
            var15.printStackTrace();
        } catch (InterruptedException var16) {
            var16.printStackTrace();
        }

        String signapk = resultPath + prefixName + "_signed.apk";
        String zipapk = "";
        zipapk = resultPath + prefixName + "_" + sdkTag + "_signed.apk";
        System.out.println("优化后的文件是：" + zipapk);
        buffer.setLength(0);
        buffer.append(Common.ZIPALIGN_PATH + " 4 " + signapk + " " + zipapk);

        try {
            Process process = Runtime.getRuntime().exec(buffer.toString(), (String[])null, ff);
            if (process.waitFor() != 0) {
                System.out.println("优化失败！！！");
            }
        } catch (IOException var13) {
            var13.printStackTrace();
        } catch (InterruptedException var14) {
            var14.printStackTrace();
        }

        System.out.println("--------------------------------");
        System.out.println("-------------签名完成-------------");
        System.out.println("--------------------------------");
        File dfile = new File(signapk);
        dfile.delete();
    }

    public static String getDate() {
        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
        String date = formater.format(new Date());
        return date;
    }

    public static boolean isFix() {
        if (Common.channlKey.keySet().toArray().length != 0) {
            Common.key = Common.channlKey.keySet().toArray();
        }

        sdkTag = CommandParamsParser.sdkTag;

        for(int i = 0; i < Common.channlKey.size(); ++i) {
            sdkName = Common.key[i].toString();
            if (sdkTag.equals(sdkName)) {
                Common.isFix = true;
                Common.valueMap = (Map)Common.channlKey.get(sdkTag);
            }
        }

        System.out.println("当前渠道是否需要特殊签名－－－isFix=" + Common.isFix);
        return Common.isFix;
    }

    public static void main(String[] args) {
        new Util();
        Util.getLocalProperty();
        new Common();
        new CommandParamsParser(args);

        try {
            apkSign(CommandParamsParser.apkPath);
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }
}
