package zhang.hy.checkNewFile;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {
	@Deprecated
	public static void copyFileByTime(String path,String reg,String aimPath,String dateStr,Map<String,String> nameList) throws ParseException, IOException{
		File file = new File(path);
		if(file.exists()) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			final long time = sdf.parse(dateStr).getTime();
			if(file.isDirectory()) {
				copyFile(file,time,reg,aimPath,nameList);
			}
		}
	}
	
	public static void copyFile(File file,final long time,String reg,String aimPath,final Map<String,String> nameList) throws IOException{
		File[] list = file.listFiles(new FileFilter(){
			@Override
			public boolean accept(File arg0) {
				long date = arg0.lastModified();
				if(date >= time)
					return true;
				return false;
			}
		});
		if(list!=null && list.length>0) {
			
			for(File f : list) {
				if(f.isDirectory()){
					copyFile(f,time,reg,aimPath,nameList);
				}else{
					String tempPath = "";
					tempPath = f.getParent().toString().replace(reg,aimPath);
					File ff = new File(tempPath);
					if(!ff.exists()) ff.mkdirs();
					File temp = new File(tempPath,f.getName());
					System.out.println(temp);
					copyOneFile(f,temp);
				}
			}
		}
	}
	
	public static void copyOneFile(File file,File aimFile) throws IOException{
		InputStream input = null;    
	    OutputStream output = null;    
	    try {
	           input = new FileInputStream(file);
	           output = new FileOutputStream(aimFile);        
	           byte[] buf = new byte[1024];        
	           int bytesRead;        
	           while ((bytesRead = input.read(buf)) > 0) {
	               output.write(buf, 0, bytesRead);
	           }
	    } finally {
	        input.close();
	        output.close();
	    }

	}
	
	public static void copyFileSByNames(String path,String reg,String aimPath,List<String> nameList) throws IOException, ParseException{
		if(nameList==null || nameList.size()==0) return;
		for (String name : nameList) {
			File file = new File(path);
			if(file.exists()) {
				copyFileByName(file,name,path,reg,aimPath);
			}
		}
	}
	
	public static void copyJspByNames(String path,String reg,String aimPath,List<String> nameList) throws IOException, ParseException{
		if(nameList==null || nameList.size()==0) return;
		for (String name : nameList) {
			File file = new File(path);
			if(file.exists()) {
				copyFileByName(file,name,path,reg,aimPath);
			}
		}
	}
	
	public static void copyFileByName(File file,final String name,String path,String reg,String aimPath) throws IOException{
		File[] list = file.listFiles(new FileFilter(){
			@Override
			public boolean accept(File arg0) {
				//System.out.println(arg0.toString());
				if(arg0.isDirectory()||arg0.getName().endsWith("class")||arg0.getName().endsWith("java")
						||arg0.getName().endsWith("jsp")||arg0.getName().endsWith("xml")
						||arg0.getName().endsWith("doc")||arg0.getName().endsWith("docx")
						||arg0.getName().endsWith("xls")||arg0.getName().endsWith("xlsx"))
					return true;
				return false;
			}
		});
		if(list!=null && list.length > 0) {
			for(File f : list) {
				if(f.isDirectory()){
					copyFileByName(f,name,path,reg,aimPath);
				}else {
					// E:/fanlei/workspace_Main_cczl/lbms-fe/bin
					String tempPath = aimPath;
			        Pattern pattern = Pattern.compile(reg);
			        Matcher matcher = pattern.matcher(f.getParentFile().getPath());
			        String tempsPath ="";
			        while(matcher.find())
			        {
			            tempsPath = matcher.group(2);
			        }
			        tempPath+=tempsPath;
					
					if(f.getName().equals(name)||(f.getName().contains(name.substring(0, name.lastIndexOf(".")))&&f.getName().contains("$"))) {
						File ff = new File(tempPath);
						if(!ff.exists()) ff.mkdirs();
						File temp = new File(tempPath,f.getName());
						System.out.println(temp);
						copyOneFile(f,temp);
					}
				}
			}
		}
	}
	
	
}

