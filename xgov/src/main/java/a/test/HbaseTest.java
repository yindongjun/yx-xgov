package a.test;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;

public class HbaseTest {
	
	private static final String zkPort = "2181";
	private static final String zkQuorum = "xian-yeexun2,xian-yeexun3,xian-yeexun4";
	private static Connection connection;
	
	static {
		Configuration conf = HBaseConfiguration.create();
	    conf.set("hbase.zookeeper.property.clientPort", zkPort);
	    conf.set("hbase.zookeeper.quorum", zkQuorum);
//	    conf.set("hbase.rootdir", "hdfs://10.221.121.2:9008/hbase");
	    try {
			connection = ConnectionFactory.createConnection(conf);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Scan scan = new Scan();
		SingleColumnValueFilter filter = equalValueFilter();
		scan.setFilter(filter);
		TableName tableName = TableName.valueOf("lj:mysql_all");
		try {
			Table table = connection.getTable(tableName);
			long start = System.currentTimeMillis();
			ResultScanner resultScanner = table.getScanner(scan);
			System.out.println("扫描完成，耗时：" + (System.currentTimeMillis() - start) / 1000 + "S.");
			int rowNum = 0;
			for (Result result : resultScanner) {
				System.out.println(++rowNum + "---" + new String(result.getRow()));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	private static SingleColumnValueFilter regexFilter() {
		RegexStringComparator regexStringComparator = new RegexStringComparator("my.");
		SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes("rk1")
				, Bytes.toBytes("a9"), CompareOp.EQUAL, regexStringComparator);
		return filter;
	}
	
	private static SingleColumnValueFilter equalValueFilter() {
		SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes("rk1")
				, Bytes.toBytes("a9"), CompareOp.EQUAL, Bytes.toBytes("sqlserver"));
		// 如果a9列不存在，不会查出这条数据。如果设置成false，如果有些数据没有a9列，那么也会认为符合条件，会被查出。
		filter.setFilterIfMissing(true);
		return filter;
	}
	
	/*private staic singlecolumnValueFilter () {
		
	}*/

}
