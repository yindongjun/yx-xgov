package a.test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cn.com.yeexun.meta_data.collector.JdbcHelper;
import cn.com.yeexun.qualityTask.entity.CheckResult;
import cn.com.yeexun.qualityTask.service.ICheckResultDetailService;

//@Controller
//@RequestMapping("/test")
public class TestJdbc {
	
//	@Autowired
	private ICheckResultDetailService checkResultDetailService;
	
	
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://10.221.121.1:3306/dyc4?useUnicode=true&characterEncoding=utf8";
		String username = "root";
		String password = "root";
		Connection connection = JdbcHelper.getConnection(driver, url, username, password);
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery("select * from dfdf limit 0,10");
		while (rs.next()) {
			System.out.println(rs.getString(1));
		}
//		StringBuilder sql = new StringBuilder("insert into check_result_detail values ");
//		StringBuilder value = new StringBuilder();
//		for (int i = 0; i < 100; i++) {
//			value.append("aaaaaaaaaaaaaaaaaaaa");
//		}
//		for (int i = 0; i < 1000; i++) {
//			sql.append("(0, 1, '" + value.toString() + "'),");
//		}
		
//		String sqlString = sql.substring(0, sql.length() - 1);
//		PreparedStatement ps = connection.prepareStatement(sqlString);
//		System.out.println("start......." + System.currentTimeMillis());
//		for (int j = 0; j < 300; j++) {
//			long start = System.currentTimeMillis();
//			StringBuilder sql = new StringBuilder("insert into check_result_detail values ");
//			StringBuilder value = new StringBuilder();
//			for (int i = 0; i < 100; i++) {
//				value.append("aaaaaaaaaaaaaaaaaaaa");
//			}
//			for (int i = 0; i < 1000; i++) {
////				sql.append("(0, 1, '" + value.toString() + "'),");
//				sql.append("(?, ?, ?),");
//			}
//			String sqlString = sql.substring(0, sql.length() - 1);
//			PreparedStatement ps = connection.prepareStatement(sqlString);
////			stmt.execute(sqlString);
//			for (int k = 0; k < 1000; k++) {
//				ps.setLong(k*3+1, 0);
//				ps.setLong(k*3+2, 1);
//				ps.setString(k*3+3, value.toString());
//			}
//			ps.execute();
//			System.out.println("第" + j + "次耗时是：" + (System.currentTimeMillis() - start));
//		}
//		System.out.println("end........." + System.currentTimeMillis());
//		
	}
	
	
	/*public static void main(String[] args) {
	    Object x = "abc";
	    System.out.println(x.getClass());
	    String x2 = get(String.class,x);
	    System.out.println(x2.getClass());
	}

	public static  <T> T get(Class<T> clz,Object o){
	    if(clz.isInstance(o)){
	        return clz.cast(o);
	    }
	    return null;
	}*/
	
//	@RequestMapping("/haha")
	public String test() {
		
		StringBuilder value = new StringBuilder();
		for (int i = 0; i < 100; i++) {
			value.append("aaaaaaaaaaaaaaaaaaaa");
		}
		
		List<CheckResult> errors = new ArrayList<>(1000);
		try {
			
			for (int i = 0; i < 300; i++) {
				for (int j = 0; j < 1000; j++) {
					CheckResult checkResult = new CheckResult();
					checkResult.setDataDetail(value.toString());
					checkResult.setResultCountId(1L);
					errors.add(checkResult);
					
				}
				long start = System.currentTimeMillis();
				checkResultDetailService.insertList(errors);
				System.out.println("第" + i + "次耗时是：" + (System.currentTimeMillis() - start));
				errors.clear();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return "ok";
		
	}
	
	/*public static void main(String[] args) throws SQLException {
		Connection connection = JdbcHelper.getConnection("oracle.jdbc.driver.OracleDriver"
				, "jdbc:oracle:thin:@10.221.121.1:1521/xe"
				, "system", "system");
		ResultSet rs = null;
		Statement stmt = null;
		String sql = "select QWA from TEST.A1";
		stmt = connection.createStatement();
		rs = stmt.executeQuery(sql);
	}*/

}
