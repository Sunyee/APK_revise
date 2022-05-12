//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package util;


import java.io.File;

import vo.Common;

public class RarUtil {
    public RarUtil() {
    }

    public static boolean delete(String source, String del) {
        String delcmd;
        if (Util.isWindowsOS()) {
            delcmd = Common.RAR_PATH + " -ibck -inul d " + source + "  " + del;
        } else {
            delcmd = " zip  -d " + source + "  " + del;
        }

        try {
            Process proc = Runtime.getRuntime().exec(delcmd);
            if (proc.waitFor() != 0 && proc.exitValue() == 0) {
                return false;
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return true;
    }

    public static boolean rar(String direct, String source) {
        if (Util.isWindowsOS()) {
            addNewFile2RarAtWin(direct, source);
        } else {
            addNewFile2RarAtLinux(direct, source);
        }

        return true;
    }

    public static boolean addNewFile2RarAtWin(String direct, String source) {
        String cmd = Common.RAR_PATH + " a -ep1 " + direct + "  " + source;

        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            if (proc.waitFor() != 0 && proc.exitValue() == 0) {
                return false;
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return true;
    }

    public static boolean addNewFile2RarAtLinux(String rarPath, String newFilePath) {
        File rarFile = new File(rarPath);
        File newFile = new File(newFilePath);
        if (rarFile.exists() && newFile.exists()) {
            String rarParent = rarFile.getParent();
            String newFileName = newFile.getName();

            try {
                String cmd_move = "  cp -R -f " + newFilePath + "   " + rarParent + File.separator;
                String cmd_cd = "cd " + rarParent + File.separator;
                String cmd_rar = "/usr/bin/zip -r " + rarPath + "  " + newFileName;
                String cmd = cmd_move + " && " + cmd_cd + " && pwd &&  " + cmd_rar;
                System.out.println("linux下添加压缩文件命令:\n " + cmd);
                String[] command = new String[]{"/bin/sh", "-c", cmd};
                Process proc = Runtime.getRuntime().exec(command);
                System.out.println(proc);
                if (proc.waitFor() != 0 && proc.exitValue() == 0) {
                    System.out.println("linux下添加压缩文件失败");
                    return false;
                }
            } catch (Exception var12) {
                var12.printStackTrace();
            }
        }

        return true;
    }

    public static boolean unRar(String apk, String rarsource, String direct) {
        String cmd = null;
        if (Util.isWindowsOS()) {
            cmd = Common.RAR_PATH + " e " + apk + " " + rarsource + " " + direct;
        } else {
            cmd = "unzip -C -j " + apk + " " + rarsource + " -d " + direct;
        }

        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            if (proc.waitFor() != 0 && proc.exitValue() == 0) {
                return false;
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return true;
    }

    public static boolean unRarAll(String apk, String direct) {
        File file = new File(direct);
        if (!file.exists()) {
            file.mkdirs();
        }

        String cmd = Common.RAR_PATH + " x -t -o-p " + apk + " " + direct;

        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            if (proc.waitFor() != 0 && proc.exitValue() == 0) {
                return false;
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return true;
    }
}
