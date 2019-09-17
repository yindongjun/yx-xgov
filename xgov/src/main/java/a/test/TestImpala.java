package a.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestImpala {
	
	//

	private static String JDBC_DRIVER = "com.cloudera.impala.jdbc41.Driver";
	private static String CONNECTION_URL = "jdbc:impala://10.172.18.4:21050/default;AuthMech=3;UID=hive;PWD=test.123!";
	//                                      jdbc:impala://10.172.18.4:21050/default;AuthMech=3;UID=hive;PWD=test.123!
	static {
		try {
			Class.forName(JDBC_DRIVER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		System.out.println("通过JDBC连接LDAP和Sentry环境下的Impala");
		Connection connection = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			connection = DriverManager.getConnection(CONNECTION_URL);
			ps = connection.prepareStatement("show tables");
			rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
}
