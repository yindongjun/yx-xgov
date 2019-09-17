package a.test.dbconnection;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

public class HbaseConnTest {
	
	public static void main(String[] args) {
		Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.property.clientPort", "2181");
        conf.set("hbase.zookeeper.quorum", "10.221.121.2");
        conf.set("hbase.rootdir", "hdfs://10.221.121.2:9000/hbase");
        
        try {
			Connection conn = ConnectionFactory.createConnection(conf);
			System.out.println(conn);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
