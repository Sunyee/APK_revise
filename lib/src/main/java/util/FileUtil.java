//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {
    public FileUtil() {
    }

    public static ArrayList<String> readAllLines(String filePath, String content) {
        if (content != null && !"".equals(content)) {
            ArrayList contentLines = new ArrayList();
            BufferedReader br = null;
            String line = null;

            try {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));

                while((line = br.readLine()) != null) {
                    if (line.contains(content)) {
                        contentLines.add(line);
                    }
                }
            } catch (Exception var17) {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException var16) {
                        br = null;
                    }
                }
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException var15) {
                        br = null;
                    }
                }

            }

            return contentLines;
        } else {
            return null;
        }
    }

    public static void copyFile(File sourceFile, File targetFile) {
        try {
            FileInputStream input = new FileInputStream(sourceFile);
            BufferedInputStream inBuff = new BufferedInputStream(input);
            FileOutputStream output = new FileOutputStream(targetFile);
            BufferedOutputStream outBuff = new BufferedOutputStream(output);
            byte[] b = new byte[5120];

            int len;
            while((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }

            outBuff.flush();
            inBuff.close();
            outBuff.close();
            output.close();
            input.close();
        } catch (Exception var8) {
            var8.printStackTrace();
        }

    }

    public static void copyDirectiory(String sourceDir, String targetDir, boolean reset) {
        (new File(targetDir)).mkdirs();
        File[] file = (new File(sourceDir)).listFiles();

        for(int i = 0; i < file.length; ++i) {
            String dir2;
            if (file[i].isFile()) {
                File sourceFile = file[i];
                dir2 = file[i].getName();
                File targetFile = new File((new File(targetDir)).getAbsolutePath() + File.separator + dir2);
                copyFile(sourceFile, targetFile);
            }

            if (file[i].isDirectory()) {
                String dir1 = sourceDir + File.separator + file[i].getName();
                dir2 = targetDir + File.separator + file[i].getName();
                copyDirectiory(dir1, dir2, reset);
            }
        }

    }

    public static String read(String filePath) {
        BufferedReader br = null;
        String line = null;
        StringBuffer buf = new StringBuffer();

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));

            while((line = br.readLine()) != null) {
                buf.append(line);
                buf.append(System.getProperty("line.separator"));
            }
        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException var12) {
                    br = null;
                }
            }

        }

        return buf.toString();
    }

    public static void write(String filePath, String content) {
        BufferedWriter bw = null;

        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
            bw.write(content);
        } catch (Exception var12) {
            var12.printStackTrace();
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException var11) {
                    bw = null;
                }
            }

        }

    }

    private static void listFolder(String path, String sStr, String xStr) {
        String ssStr = sStr.replaceAll("\\.", "/");
        String xxStr = xStr.replaceAll("\\.", "/");
        File file = new File(path);
        File[] list = file.listFiles();

        for(int i = 0; i < list.length; ++i) {
            if (list[i].isDirectory()) {
                listFolder(list[i].getPath(), sStr, xStr);
            } else {
                String filePath = list[i].getPath();
                String content = read(filePath);
                content = content.replaceAll(sStr, xStr);
                content = content.replaceAll(ssStr, xxStr);
                write(filePath, content);
            }
        }

    }

    public static void delDir(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();

                for(int i = 0; i < files.length; ++i) {
                    delDir(files[i]);
                }
            }

            file.delete();
        } else {
            System.out.println("所删除的文件不存在！\n");
        }

    }

    public static void replaceDir(File file, String content, String replacement) {
        File[] files = file.listFiles();

        for(int i = 0; i < files.length; ++i) {
            if (files[i].isDirectory()) {
                replaceDir(files[i], content, replacement);
            } else {
                String sContent = read(files[i].getAbsolutePath());
                if (sContent.contains(content)) {
                    System.out.println("找到替换内容为【" + content + "】 的文件:" + files[i].getName());
                }

                sContent = sContent.replaceAll(content, replacement);
                write(files[i].getAbsolutePath(), sContent);
            }
        }

    }

    public static void replaceDir(File file, String content, String replacement, FileFilter filter) {
        if (!file.exists()) {
            System.out.println("不存在" + file.getAbsolutePath() + "----- 已跳过处理");
        } else {
            File[] files = file.listFiles(filter);

            for(int i = 0; i < files.length; ++i) {
                if (files[i].isDirectory()) {
                    replaceDir(files[i], content, replacement, filter);
                } else {
                    String sContent = read(files[i].getAbsolutePath());
                    if (sContent.contains(content)) {
                        System.out.println("找到替换内容为【" + content + "】 的文件:" + files[i].getName());
                        sContent = sContent.replaceAll(content, replacement);
                        write(files[i].getAbsolutePath(), sContent);
                    }
                }
            }

        }
    }

    public static void delPayFiles(String filePath) {
        File root = new File(filePath);
        File[] files = root.listFiles();
        File[] var3 = files;
        int var4 = files.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            File file = var3[var5];
            if (file.isDirectory()) {
                delPayFiles(file.getAbsolutePath());
            }

            if (file.getName().equals("alipay") || file.getName().equals("unionpay")) {
                delDir(file);
            }
        }

    }

    public static void main(String[] args) {
        System.out.println("com.app.lo->" + getFilePathFrom("com.app.lo"));
    }

    public static void repleaseContent(String sdkplugin, Properties prop, Map<String, String> map) {
        String content = read(sdkplugin);
        Pattern p = null;
        Matcher m = null;
        String propKey = "";
        String propValue = "";
        Object[] mapValue = null;
        Object[] mapKey = null;
        if (map.keySet().toArray().length != 0) {
            mapValue = map.values().toArray();
            mapKey = map.keySet().toArray();
        }

        try {
            if (mapKey != null && mapValue != null) {
                for(int i = 0; i < map.size(); ++i) {
                    propKey = mapKey[i].toString();
                    propValue = mapValue[i].toString();
                    p = Pattern.compile("<meta-data android:name=\"" + propValue + "\" android:value=\"(.*?)\"");
                    m = p.matcher(content);
                    m.find();
                    String key = m.group(1);
                    System.out.println("key=========" + key);
                    content = content.replaceAll("value=\"" + key, "value=\"" + prop.getProperty(propKey));
                    System.out.println(propKey + ">>>>>>>" + propValue + "--->" + prop.getProperty(propKey));
                }
            }
        } catch (Exception var12) {
            var12.printStackTrace();
            System.out.println("配置meta值时出错:" + var12.toString());
        }

        write(sdkplugin, content);
    }

    public static List<String> readReplasePath(String filePath) {
        BufferedReader br = null;
        String line = null;
        StringBuffer buf = new StringBuffer();
        List listPath = new ArrayList();
        File file_path = new File(filePath);
        if (!file_path.isFile()) {
            System.out.println("当前读取的不是一个文件,读取失败！");
            return null;
        } else {
            try {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));

                while((line = br.readLine()) != null) {
                    buf.append(line);
                    listPath.add(line);
                }
            } catch (Exception var15) {
                var15.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException var14) {
                        br = null;
                    }
                }

            }

            return listPath;
        }
    }

    public static void renameFile(String path, String oldName, String newName) {
        if (!oldName.equals(newName)) {
            File oldfile = new File(path + "/" + oldName);
            File newfile = new File(path + "/" + newName);
            if (!oldfile.exists()) {
                System.out.println("重命名的文件不存在!");
                return;
            }

            if (newfile.exists()) {
                System.out.println(newName + "已经存在！删除" + oldfile + "文件");
                delDir(oldfile);
            } else {
                oldfile.renameTo(newfile);
            }
        } else {
            System.out.println("新文件名和旧文件名相同...");
        }

    }

    public static String readLine(String filePath, String content) {
        if (content != null && !"".equals(content)) {
            BufferedReader br = null;
            String line = null;

            try {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));

                while((line = br.readLine()) != null) {
                    if (line.contains(content)) {
                        String var4 = line;
                        return var4;
                    }
                }
            } catch (Exception var17) {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException var16) {
                        br = null;
                    }
                }
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException var15) {
                        br = null;
                    }
                }

            }

            return line;
        } else {
            return null;
        }
    }

    public static String getFilePathFrom(String name) {
        StringBuilder namePath = new StringBuilder();
        if (name != null && !"".equals(name) && name.contains(".")) {
            String[] names = name.split("\\.");

            for(int i = 0; i < names.length; ++i) {
                System.out.println("按顺序解析类名-name:" + names[i]);
                if (i == names.length - 1) {
                    namePath.append(names[i]);
                } else {
                    namePath.append(names[i] + File.separator);
                }
            }

            System.out.println(name + " 替换后的路径为：" + namePath.toString());
            return namePath.toString();
        } else {
            System.out.println("\n\n类名为空，或者不包含\".\"，无法转换！\n\n");
            return null;
        }
    }
}
