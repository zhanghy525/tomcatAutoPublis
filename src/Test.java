
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import zhang.hy.checkNewFile.FileUtil;

public class Test {
    static String propSettings = "./properties/setting.properties";//配置复制的项目及路径
    static String propPath = "./properties/copyFile.properties";//配置具体文件名称

    static Map<String,String> settings = new HashMap<String,String>();
    static Map<String,String> pathSettings = new HashMap<String,String>();
    static Map<String,String> regMap = new HashMap<String,String>();
    static Map<String,List<String>> files = new HashMap<String,List<String>>();
    static {
        Properties prop = new Properties();
        try {
            InputStream in = new FileInputStream(new File(propSettings));
            prop.load(in);
            String parentPaths = prop.getProperty("parentPaths").trim();
            List<String> setList = Arrays.asList(parentPaths.split(","));//加载文件路径列表
            List<String> keys = new ArrayList<String>();//对应的properties
            for(String path : setList) {
                if("".equals(path)) continue;
                StringBuilder tempPath = new StringBuilder(path);
                tempPath.delete(tempPath.lastIndexOf("\\"),tempPath.length());
                tempPath.delete(0,tempPath.lastIndexOf("\\")+1);
                settings.put(tempPath.toString(), path);
                keys.add(tempPath.toString());
            }
            // 加载配置复制文件和路径的关系
            String aimClassPath = prop.getProperty("aimClassPath").trim();
            String aimOtherPath = prop.getProperty("aimOtherPath").trim();
            String ace = null;
            StringBuffer realPath = null;
            for(String path : keys) {
                if("".equals(path)) continue ;
                realPath = new StringBuffer(settings.get(path));
                ace = aimClassPath;
                if(path.contains("webapps")) {
                    ace = aimOtherPath;
                }
                realPath.delete(realPath.lastIndexOf("\\"),realPath.length());
                realPath.delete(realPath.lastIndexOf("\\"),realPath.length());
                realPath.delete(0,realPath.lastIndexOf("\\")+1);
                regMap.put(path, realPath.insert(0,"(.*").append(")(.*)").toString());
                pathSettings.put(path, ace);
            }
            // 读取第二个文件
            in = new FileInputStream(new File(propPath));
            prop.load(in);
            String temp2 = null;
            for(String path : keys) {
                if("".equals(path)) continue ;
                String temp = prop.getProperty(path,"").trim();
                if("".equals(temp)) continue ;
                List<String> fileList = Arrays.asList(temp.split(","));//加载文件路径列表
                for (int i = 0; i < fileList.size(); i++) {
                    String data = (String) fileList.get(i);
                    if(data.lastIndexOf(".") > 0)
                        temp2 = data.substring(data.lastIndexOf(".")+1).trim();
                    else {
                        fileList.set(i,data+".class");
                        continue ;
                    }
                    if(temp2!=null && "class,java".lastIndexOf(temp2)>0 ) {
                        data = data.substring(0, data.lastIndexOf("."));
                        fileList.set(i,data+".class");
                        continue ;
                    }
                    fileList.set(i,data);
                }
                files.put(path, fileList);
            }
            System.out.println("loading...");
            System.out.println(settings);
            System.out.println(pathSettings);
            System.out.println(files);
            System.out.println(regMap);
            System.out.println("prepare...");
            // 数据组装完毕
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws ParseException, IOException {
        copy();
    }

    public static void copy(){
        if(files.size() <= 0) return ;
        try {
            String path = null;
            String aimPath = null;
            String reg = null;
            List<String> nameList = null;
            System.out.println("start...");
            for (String key : files.keySet()) {
                path = settings.get(key);
                aimPath = pathSettings.get(key);
                nameList = files.get(key);
                reg = regMap.get(key);
                FileUtil.copyJspByNames(path, reg, aimPath, nameList);
            }
            System.out.println("complite!");
        } catch (IOException e) {
            System.out.println("failed");
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("failed");
            e.printStackTrace();
        }
    }
}
