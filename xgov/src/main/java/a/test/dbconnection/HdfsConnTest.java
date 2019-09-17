package a.test.dbconnection;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

public class HdfsConnTest {
	
	public static void main(String[] args) {
		Configuration conf = new Configuration();
//			hdfs://10.221.121.2:9000
		conf.set("fs.defaultFS", "hdfs://10.221.121.3:8020/");
		conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");

		FileSystem fs = null;
		try {
			fs = FileSystem.get(conf);
			System.out.println(fs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
