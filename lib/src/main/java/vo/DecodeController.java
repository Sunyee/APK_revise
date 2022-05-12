//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package vo;

import java.io.File;

import util.LogUtil;

public class DecodeController {
    private String apkpath;

    public DecodeController(String apkPath) {
        this.apkpath = apkPath;
    }

    public void decode() {
        String smali_apktemp = Common.APK_TEMP + "smali_apktemp";
        System.out.println("smali_apktemp:" + smali_apktemp);
        File file = new File(smali_apktemp);
        if (!file.exists()) {
            file.mkdirs();
            StringBuffer buffer = new StringBuffer();
            buffer.setLength(0);
            String decode_command_pram = "";
            decode_command_pram = " -o ";
            String apktool = "apktool.jar";
            buffer.append(Common.JAVA).append(" -jar ").append(Common.getToolkitPath() + apktool).append(" d -f ").append(this.apkpath).append(decode_command_pram).append(smali_apktemp).append(" --only-main-classes");
            System.out.println("反编译命令:" + buffer.toString());

            try {
                Process process = Runtime.getRuntime().exec(buffer.toString());
                LogUtil.printStream(process);
                if (process.waitFor() != 0) {
                    System.out.println("反编译apk失败");
                    return;
                }
            } catch (Exception var7) {
                var7.printStackTrace();
            }

            System.out.println("反编译apk成功");
            System.out.println("第一次反编译之后复制到smali_apktemp文件中");
        } else {
            System.out.println("将之前反编译之后的smali文件复制到目标文件");
        }

        System.out.println("复制文件完成");
    }
}
