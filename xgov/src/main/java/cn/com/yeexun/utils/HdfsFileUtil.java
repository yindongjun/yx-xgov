package cn.com.yeexun.utils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HdfsFileUtil {
	//依据路径模糊查询相关的文件，返回文件名
	public static List<String> findFilePaths(String queryPath){
		String dir = queryPath.substring(0,queryPath.lastIndexOf("/"));
		String fileName = queryPath.substring(queryPath.lastIndexOf("/")+1,queryPath.length()-1);
		FileSystem fs = null;
		List<String> fileLists = new ArrayList<String>();
		try {
			Configuration conf = new Configuration();  
			fs = FileSystem.get(URI.create(dir), conf);
			FileStatus fileList[] = fs.listStatus(new Path(dir));
			if(fileList != null && fileList.length > 0){
				for(FileStatus fileStatus : fileList){
					if(fileStatus.getPath().getName().startsWith(fileName)){
						fileLists.add(fileStatus.getPath().toString());
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return fileLists;
		
	}
	
	public static void main(String[] args) {
		String path = "/datax/dest/tb_oswf_job__*";
		String dir = path.substring(0,path.lastIndexOf("/"));
		String fileName = path.substring(path.lastIndexOf("/")+1,path.indexOf("__*"));
		System.out.println(dir);
		System.out.println(fileName);
	}
}
