//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package util;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import vo.Common;

public class Util {
    public Util() {
    }

    public static void getLocalProperty() {
        Properties prop = new Properties();

        try {
            FileInputStream fis = new FileInputStream(Common.CONFIG);
            prop.load(fis);
        } catch (FileNotFoundException var2) {
            var2.printStackTrace();
        } catch (IOException var3) {
            var3.printStackTrace();
        }

        String parent = new File(System.getProperty("user.dir")).getParent();
        Common.RAR_PATH =  parent + "\\WinRAR\\WinRAR.exe";
        Common.JDK_BIN_PATH = parent + "\\JDK\\jdk\\bin";
        Common.JAVA = parent + "\\JDK\\jdk\\bin\\java.exe";
        Common.JARSIGNER = parent + "\\JDK\\jdk\\bin\\jarsigner.exe";
        Common.ZIPALIGN_PATH = System.getProperty("user.dir") + "\\toolkit\\zipalign.exe";

        Common.keyname = prop.getProperty("keyname");
        Common.storepass = prop.getProperty("storepass");
        Common.keypass = prop.getProperty("keypass");
        Common.key_alias = prop.getProperty("key_alias");
    }

    public static boolean isWindowsOS() {
        String osName = System.getProperty("os.name");
        if (osName == null) {
            System.out.println("获取OS失败");
            return false;
        } else {
            return osName.contains("Windows");
        }
    }
}
