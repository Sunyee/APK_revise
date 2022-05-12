//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtil extends Thread {
    InputStream is;
    String type;
    OutputStream os;
    private static int sdk = 0;

    public static void sdks(int sdks) {
        sdk = sdks;
    }

    public LogUtil(InputStream is, String type) {
        this(is, type, (OutputStream)null);
    }

    LogUtil(InputStream is, String type, OutputStream redirect) {
        this.is = is;
        this.type = type;
        this.os = redirect;
    }

    public void run() {
        InputStreamReader isr = null;
        BufferedReader br = null;
        PrintWriter pw = null;

        try {
            try {
                if (this.os != null) {
                    pw = new PrintWriter(this.os);
                }

                isr = new InputStreamReader(this.is);
                br = new BufferedReader(isr);

                for(String line = null; (line = br.readLine()) != null; showLog(this.type + ">" + line)) {
                    if (pw != null) {
                        pw.println(line);
                    }
                }

                if (pw != null) {
                    pw.flush();
                    pw.close();
                }

                br.close();
                isr.close();
            } catch (IOException var8) {
                var8.printStackTrace();
            }

        } finally {
            ;
        }
    }

    public static void printStream(Process proc) {
        try {
            LogUtil errorGobbler = new LogUtil(proc.getErrorStream(), "Error");
            errorGobbler.start();
            LogUtil outputGobbler = new LogUtil(proc.getInputStream(), "Output");
            outputGobbler.start();
        } catch (Exception var3) {
            showLog("获取数据流出错");
        }

    }

    public static void showLog(String msg) {
        System.out.println(getDate() + "-->" + msg);
    }

    public static String getDate() {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formater.format(new Date());
    }

    public static void log(String msg) {
        log(msg, LogLevel.LEVLE_3);
    }

    public static void log(String msg, LogLevel level) {
        StringBuffer loginfo = new StringBuffer();
        boolean isPrintLog = true;
        switch(level) {
        case LEVEL_1:
            loginfo.append(">>>>>>>>>>>>>>>").append(msg).append("<<<<<<<<<<<<<<<");
            break;
        case LEVLE_DEBUG:
            isPrintLog = true;
            loginfo.append("-->").append(msg).append("<--");
            break;
        default:
            loginfo.append("***>").append(msg).append("<***");
        }

        if (isPrintLog) {
            showLog(loginfo.toString());
        }

    }

    public static enum LogLevel {
        LEVEL_1,
        LEVLE_2,
        LEVLE_3,
        LEVLE_DEBUG;

        private LogLevel() {
        }
    }
}
