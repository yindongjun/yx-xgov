package cn.com.yeexun.utils;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

public class HdfsFileFilter implements PathFilter{
	
	private String fileName;  
	
	public HdfsFileFilter(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public boolean accept(Path path) {
		boolean res = false;
		if(path.toString().indexOf(fileName) != -1){
			res = true;
		}
		return res;
	}
	
}
