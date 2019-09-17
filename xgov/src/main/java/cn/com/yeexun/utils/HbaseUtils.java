package cn.com.yeexun.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.util.Bytes;


public class HbaseUtils {
	
	/**
	 * 获取hbase连接；
	 * @param zkquorum 集群IP；
	 * @param port 端口号；
	 * @return
	 * @throws IOException
	 */
	public static Connection getConnection(String zkquorum, String port) throws IOException {
		Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.property.clientPort", port);
        conf.set("hbase.zookeeper.quorum", zkquorum);
//        conf.set("hbase.master", "xian-yeexun5:60000");
        Connection conn = ConnectionFactory.createConnection(conf);
        return conn;
	}
	
	public static void closeConnection(Connection conn) throws IOException {
		if(conn!=null){
            conn.close();
        }
	}
	
	/**
	 * 获取Hbase Admin
	 * @param conn
	 * @return
	 * @throws IOException
	 */
	public static Admin getAdmin(Connection conn) throws IOException {
		Admin admin = conn.getAdmin();
		return admin;
	}
	
	/**
	 * 判断表空间是否存在
	 * @param conn
	 * @param admin
	 * @param nameSpace
	 * @throws IOException
	 */
	public static boolean isExistNameSpace(Admin admin, String namespace) throws IOException {
		NamespaceDescriptor[] namespaceDescriptors = admin.listNamespaceDescriptors();
        for (int i = 0; i < namespaceDescriptors.length; i++) {
			if(namespaceDescriptors[i].getName().equals(namespace)) {
				return true;
			}
		}
        return false;
	}
	
	/**
	 * 关闭admin
	 * @param admin
	 * @throws IOException
	 */
	public static void closeAdmin(Admin admin) throws IOException {
		if(admin!=null){
			admin.close();
        }
	}
	
	/**
	 * 创建命名空间；
	 * @param conn 连接对象；
	 * @param namespace 命名空间名；
	 * @throws IOException
	 */
	public static void createNamespace(Connection conn, String namespace) throws IOException {
		Admin admin = conn.getAdmin();
		admin.createNamespace(NamespaceDescriptor.create(namespace).build());
		admin.close();
	}
	
	/**
	 * 创建命名空间；
	 * @param zkquorum
	 * @param port
	 * @param namespace 命名空间名；
	 * @throws IOException
	 */
	public static void createNamespace(String zkquorum, String port, String namespace) throws IOException {
		Connection conn = getConnection(zkquorum, port);
		createNamespace(conn, namespace);
		conn.close();
	}
	
	/**
	 * 创建表；
	 * @param conn 连接对象；
	 * @param namespace 命名空间，可以为null；
	 * @param tableName 表名；
	 * @param columnFamilies 任意多个列族；
	 * @throws IOException
	 */
	public static void createTable(Connection conn, String namespace, String tableName
			, String ... columnFamilies) throws IOException {
		Admin admin = conn.getAdmin();
		TableName table = null;
		if (StringUtils.isEmpty(namespace)) {
			table = TableName.valueOf(tableName);
		} else {
			table = TableName.valueOf(namespace, tableName);
		}
		HTableDescriptor tableDescriptor = new HTableDescriptor(table);
		for (String columnFamily : columnFamilies) {
			tableDescriptor.addFamily(new HColumnDescriptor(columnFamily));
		}
		admin.createTable(tableDescriptor);
		admin.close();
	}
	
	public static void createTable(String zkquorum, String port, String namespace, String tableName
			, String ... columnFamilies) throws IOException {
		Connection conn = getConnection(zkquorum, port);
		createTable(conn, namespace, tableName, columnFamilies);
		conn.close();
	}
	
	/**
	 * 判断表是否可用；
	 * @param conn
	 * @param namespace
	 * @param tableName
	 * @return
	 * @throws IOException
	 */
	public static boolean isTableAvailable(Connection conn, String namespace, String tableName) 
			throws IOException {
		Admin admin = conn.getAdmin();
		TableName table = getTableName(namespace, tableName);
		boolean tableAvailable = admin.isTableAvailable(table);
		return tableAvailable;
	}
	
	/**
	 * 禁用表；
	 * @param conn
	 * @param namespace 可为null；
	 * @param tableName
	 * @throws IOException
	 */
	public static void disableTable(Connection conn, String namespace, String tableName) 
			throws IOException {
		Admin admin = conn.getAdmin();
		boolean tableAvailable = isTableAvailable(conn, namespace, tableName);
		if (tableAvailable) {
			admin.disableTable(getTableName(namespace, tableName));
		}
	}
	
	/**
	 * 启用表；
	 * @param conn
	 * @param namespace
	 * @param tableName
	 * @throws IOException
	 */
	public static void enableTable(Connection conn, String namespace, String tableName) 
			throws IOException {
		Admin admin = conn.getAdmin();
		boolean tableAvailable = isTableAvailable(conn, namespace, tableName);
		if (!tableAvailable) {
			admin.enableTable(getTableName(namespace, tableName));
		}
	}
	
	/**
	 * 删表；
	 * @param conn
	 * @param namespace
	 * @param tableName
	 * @throws IOException
	 */
	public static void dropTable(Connection conn, String namespace, String tableName) throws IOException {
		Admin admin = conn.getAdmin();
		TableName table = null;
		if (StringUtils.isEmpty(namespace)) {
			table = TableName.valueOf(tableName);
		} else {
			table = TableName.valueOf(namespace, tableName);
		}
		disableTable(conn, namespace, tableName);
		admin.deleteTable(table);
		conn.close();
	}
	
	/**
	 * 获取指定命名空间下的所有表名；
	 * @param zkquorum
	 * @param port
	 * @param namespace
	 * @return
	 * @throws IOException
	 */
	public static List<String> listTableNamesByNamespace(String zkquorum, String port, String namespace) 
			throws IOException {
		Connection conn = getConnection(zkquorum, port);
		Admin admin = conn.getAdmin();
		List<String> tableNameList = new ArrayList<>();
    	TableName[] tableNames = admin.listTableNamesByNamespace(namespace);
    	for (TableName tableName : tableNames) {
    		tableNameList.add(tableName.getNameAsString());
		}
    	return tableNameList;
	}
	
//	/**
//	 * 获取hbase表第一行的所有列；
//	 * @param zkquorum
//	 * @param port
//	 * @param namespace
//	 * @param tablename
//	 * @return
//	 * @throws Exception
//	 */
//	public static List<Metadata> getColumns(String zkquorum, String port, String namespace, String tablename) throws Exception {
//		List<Metadata> columnList = new ArrayList<Metadata>();
//		ResultScanner rs = null;
//		Connection con = null;
//		String columnFamily = null;
//		Scan scan = new Scan();
//		FilterList f = new FilterList(FilterList.Operator.MUST_PASS_ALL);
//		try {
//			con = getConnection(zkquorum, port);
//			TableName table = TableName.valueOf(namespace + ":" + tablename);
//			Table hTable = con.getTable(table);
//			int i = 1;// 从1开始排序
//			f.addFilter(new PageFilter(1));
//			scan.setFilter(f);
//			rs = hTable.getScanner(scan);
//			for (Result r : rs) {
//				NavigableMap<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> navigableMap = r.getMap();
//				for (Map.Entry<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> entry : navigableMap
//						.entrySet()) {
//					System.out.print(Bytes.toString(r.getRow()) + ":");
//					columnFamily = Bytes.toString(entry.getKey());
//					System.out.print("列族名称" + Bytes.toString(entry.getKey()) + "#");
//					NavigableMap<byte[], NavigableMap<Long, byte[]>> map = entry.getValue();
//					for (Map.Entry<byte[], NavigableMap<Long, byte[]>> en : map.entrySet()) {
//						Metadata column = new Metadata();
//						column.setName(columnFamily + ":" + Bytes.toString(en.getKey()));
//						column.setId(i++);
//						columnList.add(column);
//					}
//				}
//			}
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			if (con != null) {
//				con.close();
//			}
//		}
//		return columnList;
//	}


	
	
	
//	public static void dropTable(Connection conn, String tableName) throws IOException {
//		Admin admin = conn.getAdmin();
////		HTableDescriptor[] deleteTables = admin.deleteTables(tableName);
//		admin.deleteta
//	}
	
	private static TableName getTableName(String namespace, String tableName) {
		TableName table = null;
		if (StringUtils.isEmpty(namespace)) {
			table = TableName.valueOf(tableName);
		} else {
			table = TableName.valueOf(namespace, tableName);
		}
		return table;
	}
	

}
