//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package vo;

import java.io.IOException;
import java.util.Date;

import util.LogUtil;

public class BuilderController {
    private boolean first = true;

    public BuilderController() {
    }

    public boolean build(String buildPath) {
        StringBuffer buffer = new StringBuffer();
        buffer.setLength(0);
        String apktool = "apktool.jar";
        buffer.append(Common.JAVA).append(" -jar ").append(Common.getToolkitPath() + apktool).append(" b ").append(buildPath);
        System.out.println("回编译命令:" + buffer.toString());

        try {
            Process process = Runtime.getRuntime().exec(buffer.toString());
            LogUtil.printStream(process);
            if (process.waitFor() != 0) {
                System.out.println(new Date() + "编译apk失败");
                if (this.first) {
                    this.first = false;
                    return false;
                }

                return false;
            }

            System.out.println("回编译apk成功");
        } catch (IOException var5) {
            var5.printStackTrace();
        } catch (InterruptedException var6) {
            var6.printStackTrace();
        }

        return true;
    }
}
