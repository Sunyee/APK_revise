//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package vo;

import java.io.File;

public class CommandParamsParser {
    public String adbApkPath;
    public String apkName;
    public static String apkPath;
    public static String sdkTag;

    public CommandParamsParser(String[] args) {
        this.adbApkPath = args[0];
        this.apkName = args[1];
        apkPath = args[0] + File.separator + args[1];
        System.out.println(">>>>>文件全路径:【" + apkPath + "】<<<<<");
        int endIndex = this.apkName.indexOf("_");
        if (endIndex <= 0) {
            sdkTag = "yqgame";
        } else {
            sdkTag = this.apkName.substring(0, endIndex);
        }

        System.out.println("获取到的渠道名称为:" + sdkTag);
        if (apkPath == null || "".equals(apkPath)) {
            System.out.println("->SDK版本错误或者文件路径为空.请检查文件名是否遵循【规则】");
            System.exit(0);
        }

    }
}
